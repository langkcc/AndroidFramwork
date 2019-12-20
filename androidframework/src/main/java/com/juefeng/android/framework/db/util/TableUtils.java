package com.juefeng.android.framework.db.util;

import com.juefeng.android.framework.common.util.LogUtil;
import com.juefeng.android.framework.db.annotations.Column;
import com.juefeng.android.framework.db.converter.ColumnConverterFactory;
import com.juefeng.android.framework.db.entitys.ColumnEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 15:04
 * Description:
 */
public final class TableUtils {

    private TableUtils() {
    }

   public static synchronized LinkedHashMap<String, ColumnEntity> findColumnMap(Class<?> entityType) {
        LinkedHashMap<String, ColumnEntity> columnMap = new LinkedHashMap<String, ColumnEntity>();
        addColumns2Map(entityType, columnMap);
        return columnMap;
    }

    private static void addColumns2Map(Class<?> entityType, HashMap<String, ColumnEntity> columnMap) {
        if (Object.class.equals(entityType)) {
            return;
        }

        try {
            Field[] fields = entityType.getDeclaredFields();
            for (Field field : fields) {
                int modify = field.getModifiers();
                if (Modifier.isStatic(modify) || Modifier.isTransient(modify)) {
                    continue;
                }
                Column columnAnn = field.getAnnotation(Column.class);
                if (columnAnn != null) {
                    if (ColumnConverterFactory.isSupportColumnConverter(field.getType())) {
                        ColumnEntity column = new ColumnEntity(entityType, field, columnAnn);
                        if (!columnMap.containsKey(column.getName())) {
                            columnMap.put(column.getName(), column);
                        }
                    }
                }
            }

            addColumns2Map(entityType.getSuperclass(), columnMap);
        } catch (Throwable e) {
            LogUtil.e(e.getMessage(), e);
        }
    }

}
