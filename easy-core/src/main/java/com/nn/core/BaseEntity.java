package com.nn.core;

import com.nn.config.DataSourceConfig;

import javax.sql.DataSource;
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

    public Deque<Object> fieldValue =new ArrayDeque<>();


    public StringBuffer getSql() {
        return sql;
    }

    public void setSql(StringBuffer sql) {
        this.sql = sql;
    }

    public void appendSql(String str){
        this.sql.append(" %s".formatted(str));
    }
}
