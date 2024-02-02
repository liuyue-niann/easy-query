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
    private String table;
    public Deque<Object> fieldValue =new ArrayDeque<>();


    public DataSourceConfig getDatasource() {
        return datasource;
    }

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
