package com.nn.query.config;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataSourceConfig {

    public DataSource getDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/db01");
        dataSource.setUsername("postgres");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }
}
