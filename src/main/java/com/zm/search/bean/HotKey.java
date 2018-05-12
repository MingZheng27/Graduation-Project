package com.zm.search.bean;

public class HotKey {

    private String hotKey;
    private int hotLevel;

    public HotKey() {
    }

    public HotKey(String hotKey, int hotLevel) {
        this.hotKey = hotKey;
        this.hotLevel = hotLevel;
    }

    public String getHotKey() {
        return hotKey;
    }

    public void setHotKey(String hotKey) {
        this.hotKey = hotKey;
    }

    public int getHotLevel() {
        return hotLevel;
    }

    public void setHotLevel(int hotLevel) {
        this.hotLevel = hotLevel;
    }
}
