package com.trendminer.entsoe.connector.data;

import com.trendminer.entsoe.tags.model.ConnectorPoint;
import org.junit.After;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DataTransformerServiceTest {

    private static final String FIRST_ENDPOINT_PROCESS_TYPE = "A16";
    private static final String SECOND_ENDPOINT_PROCESS_TYPE = "A01";

    private static final String URL_FORMAT
            = "https://transparency.entsoe.eu/api?securityToken=2a9be2cb-d02f-4711-b9f0-5a135fbf50cb" +
            "&periodStart=202006182300" +
            "&periodEnd=202007202300" +
            "&processType=%s" +
            "&outBiddingZone_Domain=10YCA-BULGARIA-R" +
            "&documentType=A65";

    private DataTransformerService dataTransformerService = new DataTransformerService();
    private List<ConnectorPoint> connectorPoints;

    @After
    public void cleanUpConnectorPoints() {
        connectorPoints = null;
    }

    public List<ConnectorPoint> initializeConnectorPoints(String url) {
        return connectorPoints = dataTransformerService.getTransformedData(url);
    }

    @Test
    public void firstEndpointTest() {
        String url = String.format(URL_FORMAT, FIRST_ENDPOINT_PROCESS_TYPE);
        List<ConnectorPoint> pointList = initializeConnectorPoints(url);
        assertNotNull(pointList);
    }

    @Test
    public void secondEndpointTest() {
        String url = String.format(URL_FORMAT, SECOND_ENDPOINT_PROCESS_TYPE);
        List<ConnectorPoint> pointList = initializeConnectorPoints(url);
        assertNotNull(pointList);
    }

    @Test
    public void checkFirstEndpointQuantityAvailability() {
        String url = String.format(URL_FORMAT, FIRST_ENDPOINT_PROCESS_TYPE);

        List<ConnectorPoint> pointList = initializeConnectorPoints(url);

        assertEquals("3886", pointList.get(187).getValue());
        assertEquals("2959", pointList.get(384).getValue());
    }

    @Test
    public void checkSecondEndpointQuantityAvailability() {
        String url = String.format(URL_FORMAT, SECOND_ENDPOINT_PROCESS_TYPE);

        List<ConnectorPoint> pointList = initializeConnectorPoints(url);

        assertEquals("3907", pointList.get(21).getValue());
        assertEquals("4116", pointList.get(321).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenUrlIsNullThrowsException() {
        dataTransformerService.getTransformedData(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void whenUrlIsInvalidThrowsException() {
        dataTransformerService.getTransformedData(String.format("https://transparency.entsoe.eu/" +
                "api?securityToken=2a9be2cb-d02f-4711-b9f0-5a135fbf50cb" +
                "&periodStart=202006182300" +
                "&periodEnd=202007202300" +
                "&processType=%s" +
                "&outBiddingZone_Domain=10YCA-BULGARIA-R" +
                "&documentType=A65", "3000AE"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenPeriodEndIsBeforePeriodStartThrowsException() {
        dataTransformerService.getTransformedData(String.format("https://transparency.entsoe.eu/" +
                "api?securityToken=2a9be2cb-d02f-4711-b9f0-5a135fbf50cb" +
                "&periodStart=202006182300" +
                "&periodEnd=201907202300" +
                "&processType=%s" +
                "&outBiddingZone_Domain=10YCA-BULGARIA-R" +
                "&documentType=A65", FIRST_ENDPOINT_PROCESS_TYPE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenPathIsInvalidThrowsException() {
        dataTransformerService.getTransformedData(String.format("https://transparencyyyy.entsoe.eu/" +
                "api?securityToken=2a9be2cb-d02f-4711-b9f0-5a135fbf50cb" +
                "&periodStart=202006182300" +
                "&periodEnd=202005202300" +
                "&processType=%s" +
                "&outBiddingZone_Domain=10YCA-BULGARIA-R" +
                "&documentType=A65", FIRST_ENDPOINT_PROCESS_TYPE));
    }

    @Test(expected = IllegalStateException.class)
    public void whenDocumentCannotBeParsedThrowsException() {
        dataTransformerService.getTransformedData("https://transparency.entsoe.eu/");
    }
}