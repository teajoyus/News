package com.example.entry;

/**
 * Created by Administrator on 2016/8/9.
 */
public class ParamsPair {
    String key;
    String value;

    public ParamsPair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ParamsPair{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
