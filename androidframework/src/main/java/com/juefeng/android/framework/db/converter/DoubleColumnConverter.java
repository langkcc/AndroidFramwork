package com.juefeng.android.framework.db.converter;

import android.database.Cursor;
import com.juefeng.android.framework.db.sqlite.ColumnDBType;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 11:40
 * Description:
 */
public class DoubleColumnConverter implements ColumnConverter<Double> {
    @Override
    public Double getFieldValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getDouble(index);
    }

    @Override
    public Object fieldValue2DBValue(Double fieldValue) {
        return fieldValue;
    }

    @Override
    public ColumnDBType getColumnDbType() {
        return ColumnDBType.REAL;
    }
}