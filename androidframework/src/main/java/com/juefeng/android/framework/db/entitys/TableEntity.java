package com.juefeng.android.framework.db.entitys;

import android.database.Cursor;
import com.juefeng.android.framework.LKDBManager;
import com.juefeng.android.framework.common.ex.DbException;
import com.juefeng.android.framework.common.util.IOUtil;
import com.juefeng.android.framework.db.annotations.Table;
import com.juefeng.android.framework.db.util.TableUtils;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 13:48
 * Description:
 */
public class TableEntity<T> {


    /**
     * DBManager
     */
    private final LKDBManager db;
    /**
     * table name
     */
    private final String name;
    private final String onCreated;
    private ColumnEntity id;
    private Class<T> entityType;
    private Constructor<T> constructor;
    private volatile boolean checkedDatabase;

    /**
     * key: columnName
     */
    private final LinkedHashMap<String, ColumnEntity> columnMap;

    public T createEntity() throws Throwable {
        return this.constructor.newInstance();
    }

    public TableEntity(LKDBManager db, Class<T> entityType) throws Throwable {
        this.db = db;
        this.entityType = entityType;
        this.constructor = entityType.getConstructor();
        this.constructor.setAccessible(true);
        Table table = entityType.getAnnotation(Table.class);
        this.name = table.name();
        this.onCreated = table.onCreated();
        this.columnMap = TableUtils.findColumnMap(entityType);

        for (ColumnEntity column : columnMap.values()) {
            if (column.isId()) {
                this.id = column;
                break;
            }
        }
    }


    public boolean tableIsExist() throws DbException {
        if (this.isCheckedDatabase()) {
            return true;
        }

        Cursor cursor = db.execQuery("SELECT COUNT(*) AS c FROM sqlite_master WHERE type='table' AND name='" + name + "'");
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    int count = cursor.getInt(0);
                    if (count > 0) {
                        this.setCheckedDatabase(true);
                        return true;
                    }
                }
            } catch (Throwable e) {
                throw new DbException(e);
            } finally {
                IOUtil.closeQuietly(cursor);
            }
        }

        return false;
    }


    public LKDBManager getDb() {
        return db;
    }

    public String getName() {
        return name;
    }

    public Class<T> getEntityType() {
        return entityType;
    }

    public String getOnCreated() {
        return onCreated;
    }

    public ColumnEntity getId() {
        return id;
    }

    public LinkedHashMap<String, ColumnEntity> getColumnMap() {
        return columnMap;
    }

    boolean isCheckedDatabase() {
        return checkedDatabase;
    }

    public void setCheckedDatabase(boolean checkedDatabase) {
        this.checkedDatabase = checkedDatabase;
    }

    @Override
    public String toString() {
        return name;
    }
}
