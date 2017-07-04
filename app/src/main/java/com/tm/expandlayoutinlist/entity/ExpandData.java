package com.tm.expandlayoutinlist.entity;

/**
 * Created by Tian on 2017/7/3.
 */

public class ExpandData {

    private boolean isExpand;

    private float minValue = 0;
    private float maxValue = 0;

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

}
