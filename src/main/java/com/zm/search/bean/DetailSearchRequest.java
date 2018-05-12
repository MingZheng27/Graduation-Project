package com.zm.search.bean;

public class DetailSearchRequest {

    private int from;
    private int to;
    private String keyWords;

    public DetailSearchRequest() {
    }

    public DetailSearchRequest(int from, int to, String keyWords) {
        this.from = from;
        this.to = to;
        this.keyWords = keyWords;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }
}
