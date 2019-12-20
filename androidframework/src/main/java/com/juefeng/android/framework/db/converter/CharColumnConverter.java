package com.juefeng.android.framework.db.converter;

import android.database.Cursor;
import com.juefeng.android.framework.db.sqlite.ColumnDBType;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 11:44
 * Description:
 */
public class CharColumnConverter implements ColumnConverter<Character>{

    @Override
    public Character getFieldValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : (char) cursor.getInt(index);
    }

    @Override
    public Object fieldValue2DBValue(Character fieldValue) {
        if (fieldValue == null) return null;
        return (int) fieldValue;
    }

    @Override
    public ColumnDBType getColumnDbType() {
        return ColumnDBType.INTEGER;
    }
}
