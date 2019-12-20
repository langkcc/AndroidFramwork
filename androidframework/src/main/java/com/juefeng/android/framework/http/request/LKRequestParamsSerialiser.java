package com.juefeng.android.framework.http.request;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/30
 * Time: 17:32
 * Description:
 */
public class LKRequestParamsSerialiser implements JsonSerializer<LKRequestParams> {

    @Override
    public JsonElement serialize(LKRequestParams requestParams, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        Class clazz = requestParams.getValue().getClass();
        if (clazz == String.class) {
            jsonObject.addProperty(requestParams.getKey(), requestParams.getValue().toString());
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            jsonObject.addProperty(requestParams.getKey(), (Boolean) requestParams.getValue());
        } else if (clazz == int.class || clazz == Integer.class) {
            jsonObject.addProperty(requestParams.getKey(), (Integer) requestParams.getValue());
        } else if (clazz == double.class || clazz == Double.class) {
            jsonObject.addProperty(requestParams.getKey(), (Double) requestParams.getValue());
        } else if (clazz == float.class || clazz == Float.class) {
            jsonObject.addProperty(requestParams.getKey(), (Float) requestParams.getValue());
        } else if (clazz == long.class || clazz == Long.class) {
            jsonObject.addProperty(requestParams.getKey(), (Long) requestParams.getValue());
        } else if (clazz == byte.class || clazz == Byte.class) {
            jsonObject.addProperty(requestParams.getKey(), (Byte) requestParams.getValue());
        } else {
            jsonObject.addProperty(requestParams.getKey(), requestParams.getValue().toString());
        }

        if (requestParams.getArrayKey()!=null&&!requestParams.getArrayKey().equals("")){
            final JsonElement arrayElement = context.serialize(requestParams.getArrayValue());
            jsonObject.add(requestParams.getArrayKey(), arrayElement);
        }

        if (requestParams.getListKey()!=null&&!requestParams.getListKey().equals("")){
            final JsonElement listElement = context.serialize(requestParams.getListValue());
            jsonObject.add(requestParams.getListKey(), listElement);
        }

        return jsonObject;
    }
}
