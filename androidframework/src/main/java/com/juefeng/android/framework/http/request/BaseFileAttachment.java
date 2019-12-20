package com.juefeng.android.framework.http.request;

import java.io.File;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/22
 * Time: 17:48
 * Description:upload file entity
 */
public class BaseFileAttachment implements Serializable {

    private static final long serialVersionUID = -8297692606018678482L;
    private String imageId;
    private File file;

    public BaseFileAttachment() {
    }

    public BaseFileAttachment(String imageId, File file) {
        this.imageId = imageId;
        this.file = file;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
