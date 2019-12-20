package com.juefeng.android.framework.db.converter;

import android.database.Cursor;
import com.juefeng.android.framework.db.sqlite.ColumnDBType;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 11:39
 * Description:
 */
public class LongColumnConverter implements ColumnConverter<Long> {

    @Override
    public Long getFieldValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getLong(index);
    }

    @Override
    public Object fieldValue2DBValue(Long fieldValue) {
        return fieldValue;
    }

    @Override
    public ColumnDBType getColumnDbType() {
        return ColumnDBType.INTEGER;
    }
}
