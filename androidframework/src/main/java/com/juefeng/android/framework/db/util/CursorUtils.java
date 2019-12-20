package com.juefeng.android.framework.db.util;

import android.database.Cursor;
import com.juefeng.android.framework.db.entitys.ColumnEntity;
import com.juefeng.android.framework.db.entitys.DbModel;
import com.juefeng.android.framework.db.entitys.TableEntity;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 15:34
 * Description:
 */
public final class CursorUtils {
    public static <T> T getEntity(TableEntity<T> table, final Cursor cursor) throws Throwable {
        T entity = table.createEntity();
        HashMap<String, ColumnEntity> columnMap = table.getColumnMap();
        int columnCount = cursor.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            String columnName = cursor.getColumnName(i);
            ColumnEntity column = columnMap.get(columnName);
            if (column != null) {
                column.setValueFromCursor(entity, cursor, i);
            }
        }
        return entity;
    }

    public static DbModel getDbModel(final Cursor cursor) {
        DbModel result = new DbModel();
        int columnCount = cursor.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            result.add(cursor.getColumnName(i), cursor.getString(i));
        }
        return result;
    }
}
