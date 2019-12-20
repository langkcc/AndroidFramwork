package com.juefeng.android.framework;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.juefeng.android.framework.common.base.KeyValue;
import com.juefeng.android.framework.common.ex.DbException;
import com.juefeng.android.framework.db.entitys.DbModel;
import com.juefeng.android.framework.db.entitys.TableEntity;
import com.juefeng.android.framework.db.sqlite.SqlInfo;
import com.juefeng.android.framework.db.sqlite.WhereBuilder;
import com.juefeng.android.framework.db.util.Selector;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 14:15
 * Description:
 */
public interface LKDBManager extends Closeable {


    DbConfig getDaoConfig();

    SQLiteDatabase getDatabase();

    /**
     * save entity to db
     * if object'is is auto generation,then save object and generation id
     *
     * @param entity
     * @return
     * @throws DbException
     */
    boolean saveBindingId(Object entity) throws DbException;

    /**
     * save object to db.
     * if entity'id is exist,then update entity
     *
     * @param entity
     * @throws DbException
     */
    void saveOrUpdate(Object entity) throws DbException;

    /**
     * save entity to db
     *
     * @param entity
     * @throws DbException
     */
    void save(Object entity) throws DbException;


    /**
     * delete entity by idValue.
     *
     * @param entityType
     * @param idValue
     * @throws DbException
     */
    void deleteById(Class<?> entityType, Object idValue) throws DbException;

    /**
     * delete entity
     *
     * @param entity
     * @throws DbException
     */
    void delete(Object entity) throws DbException;

    /**
     * delete clear entity type of table
     *
     * @param entityType
     * @throws DbException
     */
    void delete(Class<?> entityType) throws DbException;

    int delete(Class<?> entityType, WhereBuilder whereBuilder) throws DbException;

    ///////////// update
    void update(Object entity, String... updateColumnNames) throws DbException;

    int update(Class<?> entityType, WhereBuilder whereBuilder, KeyValue... nameValuePairs) throws DbException;

    /**
     * select by id
     *
     * @param entityType
     * @param idValue
     * @param <T>
     * @return
     * @throws DbException
     */
    <T> T findById(Class<T> entityType, Object idValue) throws DbException;

    /**
     * select first entity
     *
     * @param entityType
     * @param <T>
     * @return
     * @throws DbException
     */
    <T> T findFirst(Class<T> entityType) throws DbException;

    /**
     * select all entity
     *
     * @param entityType
     * @param <T>
     * @return
     * @throws DbException
     */
    <T> List<T> findAll(Class<T> entityType) throws DbException;

    <T> Selector<T> selector(Class<T> entityType) throws DbException;

    DbModel findDbModelFirst(SqlInfo sqlInfo) throws DbException;

    List<DbModel> findDbModelAll(SqlInfo sqlInfo) throws DbException;

    /**
     * get table entity from pojo
     *
     * @param entityType
     * @param <T>
     * @return
     * @throws DbException
     */
    <T> TableEntity<T> getTable(Class<T> entityType) throws DbException;

    /**
     * drop table by pojo
     *
     * @param entityType
     * @throws DbException
     */
    void dropTable(Class<?> entityType) throws DbException;

    /**
     * add column
     * entityType must definition this column
     *
     * @param entityType
     * @param column
     * @throws DbException
     */
    void addColumn(Class<?> entityType, String column) throws DbException;

    ///////////// db

    /**
     * delete db
     *
     * @throws DbException
     */
    void dropDb() throws DbException;

    /**
     * close db
     * default is single instance,don't need close
     *
     * @throws IOException
     */
    void close() throws IOException;

    /**
     * execute sqlInfo,return change lines
     *
     * @param sqlInfo
     * @return
     * @throws DbException
     */
    int executeUpdateDelete(SqlInfo sqlInfo) throws DbException;

    /**
     * execute sql,return change lines
     *
     * @param sql
     * @return
     * @throws DbException
     */
    int executeUpdateDelete(String sql) throws DbException;

    /**
     * execute sql,don't need return change lines
     *
     * @param sqlInfo
     * @throws DbException
     */
    void execNonQuery(SqlInfo sqlInfo) throws DbException;

    void execNonQuery(String sql) throws DbException;

    Cursor execQuery(SqlInfo sqlInfo) throws DbException;

    Cursor execQuery(String sql) throws DbException;


    public interface DbOpenListener {
        void onDbOpened(LKDBManager db);
    }

    public interface DbUpgradeListener {
        void onUpgrade(LKDBManager db, int oldVersion, int newVersion);
    }

    public interface TableCreateListener {
        void onTableCreated(LKDBManager db, TableEntity table);
    }

    /**
     * DB default config
     */
    public static class DbConfig {
        /**
         * db file
         */
        private File dbDir;
        /**
         * db default name
         */
        private String dbName = "lk_application.db"; // default db name
        /**
         * db version
         */
        private int dbVersion = 1;
        /**
         * db transaction
         */
        private boolean allowTransaction = true;
        private DbUpgradeListener dbUpgradeListener;
        private TableCreateListener tableCreateListener;
        private DbOpenListener dbOpenListener;

        public DbConfig() {
        }

        public File getDbDir() {
            return dbDir;
        }

        public DbConfig setDbDir(File dbDir) {
            this.dbDir = dbDir;
            return this;
        }

        public String getDbName() {
            return dbName;
        }

        public DbConfig setDbName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public int getDbVersion() {
            return dbVersion;
        }

        public DbConfig setDbVersion(int dbVersion) {
            this.dbVersion = dbVersion;
            return this;
        }

        public boolean isAllowTransaction() {
            return allowTransaction;
        }

        public DbConfig setAllowTransaction(boolean allowTransaction) {
            this.allowTransaction = allowTransaction;
            return this;
        }

        public DbUpgradeListener getDbUpgradeListener() {
            return dbUpgradeListener;
        }

        public DbConfig setDbUpgradeListener(DbUpgradeListener dbUpgradeListener) {
            this.dbUpgradeListener = dbUpgradeListener;
            return this;

        }

        public TableCreateListener getTableCreateListener() {
            return tableCreateListener;
        }

        public DbConfig setTableCreateListener(TableCreateListener tableCreateListener) {
            this.tableCreateListener = tableCreateListener;
            return this;

        }

        public DbOpenListener getDbOpenListener() {
            return dbOpenListener;
        }

        public DbConfig setDbOpenListener(DbOpenListener dbOpenListener) {
            this.dbOpenListener = dbOpenListener;
            return this;

        }
    }

}
