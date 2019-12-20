package com.juefeng.androidframework.entity;

import com.juefeng.android.framework.update.ResultUpdateVersion;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/12/15
 * Time: 16:49
 * Description:
 */
public class VersionUpdateBean implements ResultUpdateVersion {

    private String verName;
    private int verCode;
    private String fileSize;
    private String url;
    private boolean isUpdate;
    private String desc;

    public VersionUpdateBean() {
        super();
    }

    @Override
    public boolean isForceUpdate() {
        return isUpdate;
    }

    @Override
    public boolean isUpdate() {
        int version = 1;
        if (version == verCode) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String getContent() {
        return this.desc;
    }

    @Override
    public String getDownlogUrl() {
        return this.url;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public int getVerCode() {
        return verCode;
    }

    public void setVerCode(int verCode) {
        this.verCode = verCode;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
