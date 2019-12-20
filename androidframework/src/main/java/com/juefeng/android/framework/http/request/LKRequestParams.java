package com.juefeng.android.framework.http.request;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/22
 * Time: 17:49
 * Description:Request Params entity
 */
public class LKRequestParams<T> {


    /**
     * request key
     */
    private java.lang.String key;

    /**
     * request value
     */
    private T value;

    private List<LKRequestParams> listValue;

    private String listKey;

    private String arrayKey;

    private List arrayValue;

    public LKRequestParams() {
    }

    /**
     * @param key   request key
     * @param value request value
     */
    public LKRequestParams(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }


    public List<LKRequestParams> getListValue() {
        return listValue;
    }

    public void setListValue(List<LKRequestParams> listValue) {
        this.listValue = listValue;
    }

    public String getListKey() {
        return listKey;
    }

    public void setListKey(String listKey) {
        this.listKey = listKey;
    }

    public String getArrayKey() {
        return arrayKey;
    }

    public void setArrayKey(String arrayKey) {
        this.arrayKey = arrayKey;
    }

    public List getArrayValue() {
        return arrayValue;
    }

    public void setArrayValue(List arrayValue) {
        this.arrayValue = arrayValue;
    }
}
