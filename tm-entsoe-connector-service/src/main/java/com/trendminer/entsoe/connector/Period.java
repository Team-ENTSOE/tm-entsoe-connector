package com.trendminer.entsoe.connector;

import com.trendminer.entsoe.tags.model.ConnectorPoint;

public class Period {

    private final TimeInterval timeInterval;

    private final ConnectorPoint[] points;

    public Period(TimeInterval timeInterval, ConnectorPoint[] points) {
        this.timeInterval = timeInterval;
        this.points = points;
    }

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public ConnectorPoint[] getPoints() {
        return points;
    }
}
