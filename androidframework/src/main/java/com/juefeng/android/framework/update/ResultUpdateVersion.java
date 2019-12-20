package com.juefeng.android.framework.update;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/12/15
 * Time: 15:30
 * Description:
 */
public interface ResultUpdateVersion {

    /**
     * 是否强制更新
     * @return
     */
    boolean isForceUpdate();

    /**
     * 是否有可用更新
     * @return
     */
    boolean isUpdate();

    /**
     * 获取更新内容
     * @return
     */
    String getContent();

    /**
     * 获取下载地址
     * @return
     */
    String getDownlogUrl();

}
