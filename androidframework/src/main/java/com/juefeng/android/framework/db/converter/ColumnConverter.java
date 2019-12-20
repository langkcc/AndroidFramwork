package com.juefeng.android.framework.db.converter;

import android.database.Cursor;
import com.juefeng.android.framework.db.sqlite.ColumnDBType;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 11:23
 * Description:
 */
public interface ColumnConverter<T> {

    Object fieldValue2DBValue(T fieldValue);

    T getFieldValue(Cursor cursor,int index);

    ColumnDBType getColumnDbType();


}
