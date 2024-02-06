package com.nn.query.config;


import javax.sql.DataSource;

public class DataSourceConfig {


    private static DataSource dataSource;

    public final DataSource getDataSource() {
        return dataSource;
    }


    public final void setDatasource(DataSource dataSource) {
        DataSourceConfig.dataSource = dataSource;
    }
}
