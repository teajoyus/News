package com.example.entry;

import com.example.util.NewsSharedPreferences;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
public class Label {
    private String key;
    private String name;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Label{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

}
