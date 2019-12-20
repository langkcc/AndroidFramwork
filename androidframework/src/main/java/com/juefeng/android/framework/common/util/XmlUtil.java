package com.juefeng.android.framework.common.util;

import android.app.Application;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Xml;
import com.juefeng.android.framework.http.annotations.BaseElement;
import com.juefeng.android.framework.http.annotations.Key;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/29
 * Time: 15:13
 * Description:xml util
 */
public class XmlUtil {


    /**
     * parse xml content
     *
     * @param content
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseXml(String content, Class<T> clazz) {
        try {
            if (content == null || content.isEmpty()) {
                return null;
            }
            T t = clazz.newInstance();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            ByteArrayInputStream strm = new ByteArrayInputStream(content.getBytes());
            Document document = builder.parse(strm);
            Element element = document.getDocumentElement();
            BaseElement baseElement = (BaseElement) clazz.getAnnotation(BaseElement.class);
            if (!element.getNodeName().equals(baseElement.key())) {
                return null;
            }
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Key key = field.getAnnotation(Key.class);
                String keycontent = element.getElementsByTagName(key.key()).item(0).getTextContent();
                if (field.getType() == String.class) {
                    field.set(t, keycontent);
                } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                    field.set(t, Boolean.parseBoolean(keycontent));
                } else if (field.getType() == int.class || field.getType() == Integer.class) {
                    field.set(t, Integer.parseInt(keycontent));
                } else if (field.getType() == double.class || field.getType() == Double.class) {
                    field.set(t, Double.parseDouble(keycontent));
                } else if (field.getType() == float.class || field.getType() == Float.class) {
                    field.set(t, Float.parseFloat(keycontent));
                } else if (field.getType() == long.class || field.getType() == Long.class) {
                    field.set(t, Long.parseLong(keycontent));
                } else if (field.getType() == byte.class || field.getType() == Byte.class) {
                    field.set(t, Byte.parseByte(keycontent));
                }
            }
            if (strm != null) {
                strm.close();
            }
            return t;
        } catch (SAXException e) {
            LogUtil.w(e);
        } catch (IOException e) {
            LogUtil.w(e);
        } catch (ParserConfigurationException e) {
            LogUtil.w(e);
        } catch (IllegalAccessException e) {
            LogUtil.w(e);
        } catch (InstantiationException e) {
            LogUtil.w(e);
        }
        return null;
    }


    /**
     * generation xml content
     *
     * @param t
     * @return String
     */
    public static <T> boolean generationXml(T t, File file) {
        try {
            Class clazz = t.getClass();
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            // generation serializer
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "utf-8");
            // set header
            serializer.startDocument("utf-8", true);
            BaseElement baseElement = (BaseElement) clazz.getAnnotation(BaseElement.class);
            serializer.startTag(null, baseElement.key());
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Key key = field.getAnnotation(Key.class);
                serializer.startTag(null, key.key());
                serializer.text(field.get(t).toString());
                serializer.endTag(null, key.key());
            }
            serializer.endTag(null, baseElement.key());
            serializer.endDocument();
            if (fos != null) {
                fos.close();
            }
            return true;
        } catch (IOException e) {
            LogUtil.w(e);
        } catch (IllegalAccessException e) {
            LogUtil.w(e);
        }
        return false;
    }

}
