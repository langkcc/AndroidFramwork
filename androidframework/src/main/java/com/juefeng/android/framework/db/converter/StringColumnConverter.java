package com.juefeng.android.framework.db.converter;

import android.database.Cursor;
import com.juefeng.android.framework.db.sqlite.ColumnDBType;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 11:36
 * Description:
 */
public class StringColumnConverter implements ColumnConverter<String> {

    @Override
    public Object fieldValue2DBValue(String fieldValue) {
        return fieldValue;
    }

    @Override
    public String getFieldValue(Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getString(index);
    }

    @Override
    public ColumnDBType getColumnDbType() {
        return ColumnDBType.TEXT;
    }
}
