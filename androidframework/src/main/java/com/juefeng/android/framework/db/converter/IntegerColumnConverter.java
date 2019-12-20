package com.juefeng.android.framework.db.converter;

import android.database.Cursor;
import com.juefeng.android.framework.db.sqlite.ColumnDBType;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 11:38
 * Description:
 */
public class IntegerColumnConverter implements ColumnConverter<Integer> {
    @Override
    public Object fieldValue2DBValue(Integer fieldValue) {
        return fieldValue;
    }

    @Override
    public Integer getFieldValue(Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getInt(index);
    }

    @Override
    public ColumnDBType getColumnDbType() {
        return ColumnDBType.INTEGER;
    }
}
