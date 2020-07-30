package com.trendminer.entsoe.connector.data;

import com.trendminer.entsoe.connector.Period;
import com.trendminer.entsoe.tags.model.ConnectorPoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

public class DataTransformerService {

    DataExtractor dataExtractor = new DataExtractor();

    List<ConnectorPoint> getTransformedData(String url) throws IllegalArgumentException, IllegalStateException {

        if (url == null) {
            throw new IllegalArgumentException("The given URL is empty! Please enter some URL.");
        }

        Document document;
        try {
            document = initializeDocument(url);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalArgumentException("The given URL is invalid! Please check the URL.");
        } catch (ResourceAccessException e) {
            throw new IllegalArgumentException("Invalid URL path!");
        }

        NodeList periodList = document.getElementsByTagName("Period");

        if (periodList == null || periodList.getLength() == 0) {
            throw new IllegalStateException("There are no periods in the XML!");
        }

        List<Period> periodsData = dataExtractor.getPeriodsData(periodList);

        List<ConnectorPoint> allConnectorPoints = new ArrayList<>();

        for (Period period : periodsData) {
            allConnectorPoints.addAll(Arrays.asList(period.getPoints()));
        }

        return allConnectorPoints;
    }

    private Document initializeDocument(String url) throws HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);

        String body = response.getBody();
        return dataExtractor.getDocument(body);
    }
}
