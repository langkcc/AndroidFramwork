package com.juefeng.android.framework.db.converter;

import com.juefeng.android.framework.common.util.LogUtil;
import com.juefeng.android.framework.db.sqlite.ColumnDBType;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 12:56
 * Description:
 */
public final class ColumnConverterFactory {

    private ColumnConverterFactory() {
    }

    public static ColumnConverter getColumnConverter(Class columnType) {
        ColumnConverter result = null;
        if (columnType_columnConverter_map.containsKey(columnType.getName())) {
            result = columnType_columnConverter_map.get(columnType.getName());
        } else if (ColumnConverter.class.isAssignableFrom(columnType)) {
            try {
                ColumnConverter columnConverter = (ColumnConverter) columnType.newInstance();
                if (columnConverter != null) {
                    columnType_columnConverter_map.put(columnType.getName(), columnConverter);
                }
                result = columnConverter;
            } catch (Throwable ex) {
                LogUtil.e(ex.getMessage(), ex);
            }
        }

        if (result == null) {
            throw new RuntimeException("Database Column Not Support: " + columnType.getName() +
                    ", please impl ColumnConverter or use ColumnConverterFactory#registerColumnConverter(...)");
        }

        return result;
    }

    public static ColumnDBType getDBColumnType(Class columnType) {
        ColumnConverter converter = getColumnConverter(columnType);
        return converter.getColumnDbType();
    }

    public static void registerColumnConverter(Class columnType, ColumnConverter columnConverter) {
        columnType_columnConverter_map.put(columnType.getName(), columnConverter);
    }

    public static boolean isSupportColumnConverter(Class columnType) {
        if (columnType_columnConverter_map.containsKey(columnType.getName())) {
            return true;
        } else if (ColumnConverter.class.isAssignableFrom(columnType)) {
            try {
                ColumnConverter columnConverter = (ColumnConverter) columnType.newInstance();
                if (columnConverter != null) {
                    columnType_columnConverter_map.put(columnType.getName(), columnConverter);
                }
                return columnConverter == null;
            } catch (Throwable e) {
            }
        }
        return false;
    }

    private static final ConcurrentHashMap<String, ColumnConverter> columnType_columnConverter_map;

    static {
        columnType_columnConverter_map = new ConcurrentHashMap<String, ColumnConverter>();

        BooleanColumnConverter booleanColumnConverter = new BooleanColumnConverter();
        columnType_columnConverter_map.put(boolean.class.getName(), booleanColumnConverter);
        columnType_columnConverter_map.put(Boolean.class.getName(), booleanColumnConverter);

        CharColumnConverter charColumnConverter = new CharColumnConverter();
        columnType_columnConverter_map.put(char.class.getName(), charColumnConverter);
        columnType_columnConverter_map.put(Character.class.getName(), charColumnConverter);

        DateColumnConverter dateColumnConverter = new DateColumnConverter();
        columnType_columnConverter_map.put(Date.class.getName(), dateColumnConverter);

        DoubleColumnConverter doubleColumnConverter = new DoubleColumnConverter();
        columnType_columnConverter_map.put(double.class.getName(), doubleColumnConverter);
        columnType_columnConverter_map.put(Double.class.getName(), doubleColumnConverter);

        FloatColumnConverter floatColumnConverter = new FloatColumnConverter();
        columnType_columnConverter_map.put(float.class.getName(), floatColumnConverter);
        columnType_columnConverter_map.put(Float.class.getName(), floatColumnConverter);

        IntegerColumnConverter integerColumnConverter = new IntegerColumnConverter();
        columnType_columnConverter_map.put(int.class.getName(), integerColumnConverter);
        columnType_columnConverter_map.put(Integer.class.getName(), integerColumnConverter);

        LongColumnConverter longColumnConverter = new LongColumnConverter();
        columnType_columnConverter_map.put(long.class.getName(), longColumnConverter);
        columnType_columnConverter_map.put(Long.class.getName(), longColumnConverter);

        StringColumnConverter stringColumnConverter = new StringColumnConverter();
        columnType_columnConverter_map.put(String.class.getName(), stringColumnConverter);
    }
}
