package com.es.phoneshop.security;

public class RequestInfo {
    private long count;

    private long timestamp;

    public RequestInfo(long count, long timestamp) {
        this.count = count;
        this.timestamp = timestamp;
    }

    public RequestInfo() {
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void incCount() {
        ++count;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
