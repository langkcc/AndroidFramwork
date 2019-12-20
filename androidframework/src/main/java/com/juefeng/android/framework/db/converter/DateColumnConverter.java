package com.juefeng.android.framework.db.converter;

import android.database.Cursor;
import com.juefeng.android.framework.db.sqlite.ColumnDBType;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 11:42
 * Description:
 */
public class DateColumnConverter implements ColumnConverter<Date> {
    @Override
    public Date getFieldValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : new Date(cursor.getLong(index));
    }

    @Override
    public Object fieldValue2DBValue(Date fieldValue) {
        if (fieldValue == null) return null;
        return fieldValue.getTime();
    }

    @Override
    public ColumnDBType getColumnDbType() {
        return ColumnDBType.INTEGER;
    }
}
