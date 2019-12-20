package com.juefeng.android.framework.db.sqlite;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 11:28
 * Description:
 */
public enum ColumnDBType {

    INTEGER("INTEGER"),REAL("REAL"),TEXT("TEXT"),BLOB("BLOB");

    private String value;

    ColumnDBType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
