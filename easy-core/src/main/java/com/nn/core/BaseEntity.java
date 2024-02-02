package com.nn.core;

import com.nn.config.DataSourceConfig;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author niann
 * @date 2024/2/2 10:14
 * @description
 **/
public class BaseEntity {
    private final DataSourceConfig datasource = new DataSourceConfig();

    private StringBuffer sql;
    private String tableName;
    private Class<?> table;
    private String tableId;
    private Deque<Object> fieldValue = new ArrayDeque<>();


    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public Deque<Object> getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Deque<Object> fieldValue) {
        this.fieldValue = fieldValue;
    }

    public DataSourceConfig getDatasource() {
        return datasource;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Class<?> getTable() {
        return table;
    }

    public void setTable(Class<?> table) {
        this.table = table;
    }


    public StringBuffer getSql() {
        return sql;
    }

    public void setSql(StringBuffer sql) {
        this.sql = sql;
    }

    public void setSql(String sql) {
        this.sql = new StringBuffer(sql);
    }

    public void appendSql(String str) {
        this.sql.append(" %s".formatted(str));
    }
}
