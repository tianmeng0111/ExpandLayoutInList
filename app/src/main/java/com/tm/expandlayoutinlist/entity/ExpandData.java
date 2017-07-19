package com.tm.expandlayoutinlist.entity;

/**
 * Created by Tian on 2017/7/3.
 */

public class ExpandData {

    private boolean isExpand;

    private float minValue = 0;
    private float maxValue = 0;

    private String minString;

    private boolean isLineFeed = false;

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public String getMinString() {
        return minString;
    }

    public void setMinString(String minString) {
        this.minString = minString;
    }

    public boolean isLineFeed() {
        return isLineFeed;
    }

    public void setLineFeed(boolean lineFeed) {
        isLineFeed = lineFeed;
    }
}
