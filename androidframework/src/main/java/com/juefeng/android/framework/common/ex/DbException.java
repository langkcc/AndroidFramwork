package com.juefeng.android.framework.common.ex;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 14:28
 * Description:
 */
public class DbException extends RuntimeException {

    public DbException() {
        super();
    }

    public DbException(Throwable e) {
        super(e);
    }

    public DbException(String s) {
        super(s);
    }
}
