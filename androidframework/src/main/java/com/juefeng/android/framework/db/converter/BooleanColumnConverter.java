package com.juefeng.android.framework.db.converter;

import android.database.Cursor;
import com.juefeng.android.framework.db.sqlite.ColumnDBType;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 11:34
 * Description:
 */
public class BooleanColumnConverter implements ColumnConverter<Boolean> {

    @Override
    public Object fieldValue2DBValue(Boolean fieldValue) {
        if (fieldValue == null) return null;
        return fieldValue ? 1 : 0;
    }

    @Override
    public Boolean getFieldValue(Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getInt(index) == 1;
    }

    @Override
    public ColumnDBType getColumnDbType() {
        return ColumnDBType.INTEGER;
    }
}
