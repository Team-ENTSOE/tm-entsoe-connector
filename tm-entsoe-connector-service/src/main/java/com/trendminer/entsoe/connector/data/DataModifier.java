package com.trendminer.entsoe.connector.data;

import com.trendminer.entsoe.tags.model.ConnectorPoint;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DataModifier {
    private static final String EMPTY_ERROR_MESSAGE = " is empty!";

    List<ConnectorPoint> modifyDataToConnectorPoint(List<Node> allPointNodes, String start) throws IllegalStateException {
        List<ConnectorPoint> pointsList = new ArrayList<>();
        for (Node node : allPointNodes) {
            NodeList pointChildNodes = node.getChildNodes();

            if (pointChildNodes == null || pointChildNodes.getLength() == 0) {
                throw new IllegalStateException("There are no elements in <Point>!");
            }

            Node positionNode = pointChildNodes.item(1);
            Node quantityNode = pointChildNodes.item(3);

            if (positionNode == null) {
                throw new IllegalStateException("<position>" + EMPTY_ERROR_MESSAGE);
            }
            if (quantityNode == null) {
                throw new IllegalStateException("<quantity>" + EMPTY_ERROR_MESSAGE);
            }

            String position = positionNode.getTextContent();
            String quantity = quantityNode.getTextContent();

            long hours;
            try {
                hours = Long.parseLong(position);
            } catch (NumberFormatException e) {
                throw new IllegalStateException("<position> is invalid number format!");
            }

            LocalDateTime localDateTime = getCalculatedDateTime(hours, start);

            String ts = localDateTime.toString() + ".0000000Z";

            pointsList.add(new ConnectorPoint(ts, quantity));
        }

        return pointsList;
    }

    private LocalDateTime getCalculatedDateTime(long hours, String start) {
        LocalDateTime localDateTime = parsePeriodStart(start);
        localDateTime = localDateTime.plusHours(hours);
        return localDateTime;
    }

    private LocalDateTime parsePeriodStart(String start) throws IllegalStateException {
        LocalDateTime parsed;
        try {
            parsed = LocalDateTime.parse(start, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new IllegalStateException("Date cannot be parsed!");
        }

        return parsed;
    }
}
