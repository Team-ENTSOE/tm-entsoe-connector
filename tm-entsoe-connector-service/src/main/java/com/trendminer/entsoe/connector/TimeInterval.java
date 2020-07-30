package com.trendminer.entsoe.connector;

public class TimeInterval {

    private final String start;

    private final String end;

    public TimeInterval(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
}
