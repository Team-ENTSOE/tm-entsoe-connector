package com.trendminer.entsoe.connector.data;

import com.google.common.collect.Iterables;
import com.trendminer.entsoe.connector.Period;
import com.trendminer.entsoe.connector.TimeInterval;
import com.trendminer.entsoe.tags.model.ConnectorPoint;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class DataExtractor {

    private static final String TIME_INTERVAL_ERROR_MESSAGE = " time interval is empty!";

    private DataModifier dataModifier = new DataModifier();

    Document getDocument(String body) throws IllegalArgumentException, IllegalStateException {
        Document document;

        if (body == null) {
            throw new IllegalArgumentException("The response body is \"null\"!");
        }

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();

            InputSource inputSource = new InputSource(new StringReader(body));
            document = documentBuilder.parse(inputSource);

            if (document == null) {
                throw new IllegalArgumentException("The document is \"null\"!");
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new IllegalStateException("Document cannot be parsed!");
        }

        return document;
    }

    List<Period> getPeriodsData(NodeList periodNodes) throws IllegalStateException {
        List<Period> periodList = new ArrayList<>();

        for (int i = 0; i < periodNodes.getLength(); i++) {
            Node periodNode = periodNodes.item(i);
            NodeList periodChildNodes = periodNode.getChildNodes();

            if (periodChildNodes == null || periodChildNodes.getLength() == 0) {
                throw new IllegalStateException("There are no elements in <Period>!");
            }

            List<Node> allPointNodes = getNodesByName(periodChildNodes, "Point");

            if (allPointNodes.size() == 0) {
                throw new IllegalStateException("There are no <Point> elements in <Period>!");
            }

            TimeInterval timeIntervalObj = getTimeInterval(periodChildNodes);

            List<ConnectorPoint> pointList = dataModifier
                    .modifyDataToConnectorPoint(allPointNodes, timeIntervalObj.getStart());

            ConnectorPoint[] connectorPointsArr = Iterables.toArray(pointList, ConnectorPoint.class);
            periodList.add(
                    new Period(timeIntervalObj, connectorPointsArr)
            );
        }

        return periodList;
    }

    private TimeInterval getTimeInterval(NodeList nodeList) throws IllegalStateException {
        List<Node> timeIntervalNodes = getNodesByName(nodeList, "timeInterval");

        Node timeIntervalNode = timeIntervalNodes.get(0);
        NodeList timeIntervalChildNodes = timeIntervalNode.getChildNodes();

        String start = timeIntervalChildNodes.item(1).getTextContent();
        String end = timeIntervalChildNodes.item(3).getTextContent();

        if (start == null) {
            throw new IllegalStateException("Start" + TIME_INTERVAL_ERROR_MESSAGE);
        }
        if (end == null) {
            throw new IllegalStateException("End" + TIME_INTERVAL_ERROR_MESSAGE);
        }

        return new TimeInterval(start, end);
    }

    private List<Node> getNodesByName(NodeList nodeList, String nodeName) {
        List<Node> allNodes = new ArrayList<>();

        for (int k = 0; k < nodeList.getLength(); k++) {
            Node child = nodeList.item(k);
            String childNodeName = child.getNodeName();

            if (childNodeName.equals(nodeName)) {
                allNodes.add(child);
            }
        }

        return allNodes;
    }
}
