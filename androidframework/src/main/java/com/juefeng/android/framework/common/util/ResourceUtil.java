package com.juefeng.android.framework.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/30
 * Time: 10:22
 * Description:
 */
public class ResourceUtil {


    private Context context;

    /**
     * Resource util
     *
     * @param context
     */
    public ResourceUtil(Context context) {
        this.context = context;
    }


    /**
     * get xml file id
     *
     * @param resName
     * @return
     */
    public int getXMLResourceInteger(String resName) {
        int iId = -1;
        try {
            String resType = "xml";
            iId = parseResIdByResNameAndResType(resName, resType);
        } catch (Exception e) {
            iId = -1;
            LogUtil.w("getXMLResourceInteger error", e);
        }
        return iId;
    }


    /**
     * parse resource file id of resType
     *
     * @param resName
     * @param resType
     * @return
     */
    private int parseResIdByResNameAndResType(String resName, String resType) {
        int iId = -1;
        if (!StringUtil.isEmpty(resName)) {
            Resources resources = this.context.getResources();
            iId = resources.getIdentifier(resName, resType,
                    this.context.getPackageName());
        }
        return iId;
    }

    /**
     * get string
     *
     * @param context
     * @param name
     * @return
     */
    public static String getString(Context context, String name) {
        try {
            int id = context.getResources().getIdentifier(name, "string", context.getPackageName());
            return context.getResources().getString(id);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * get layout
     *
     * @param context
     * @param name
     * @return
     */
    public static XmlResourceParser getLayout(Context context, String name) {
        try {
            int id = context.getResources().getIdentifier(name, "layout", context.getPackageName());
            return context.getResources().getLayout(id);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * get id
     *
     * @param context
     * @param name
     * @return
     */
    public static int getId(Context context, String name) {
        try {
            int id = context.getResources().getIdentifier(name, "layout", context.getPackageName());
            return context.getResources().getIdentifier(name,"id",context.getPackageName());
        } catch (Exception e) {
            return -1;
        }
    }
}
