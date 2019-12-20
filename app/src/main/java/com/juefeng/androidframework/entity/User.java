package com.juefeng.androidframework.entity;

import com.juefeng.android.framework.common.base.BaseEntity;
import com.juefeng.android.framework.db.annotations.Column;
import com.juefeng.android.framework.db.annotations.Table;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/25
 * Time: 15:13
 * Description:
 */
@Table(name = "user")
public class User extends BaseEntity{

    @Column(name = "name",isId = true)
    private String name;

    @Column(name = "passwd")
    private String passwd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
