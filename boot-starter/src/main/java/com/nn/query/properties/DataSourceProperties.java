package com.nn.query.properties;

import com.nn.query.config.DataSourceConfig;
import com.nn.query.exception.DataSourceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

/**
 * @author niann
 * @date 2024/2/6 21:43
 * @description
 **/

@ConfigurationProperties("easy.query")
public class DataSourceProperties {


    public DataSourceProperties(@Value("${spring.datasource.username}") String username,
                                @Value("${spring.datasource.password}") String password,
                                @Value("${spring.datasource.url}") String url,
                                @Value("${spring.datasource.driver-class-name}") String driver) {

        if (driver == null || driver.isEmpty() || driver.isBlank()) {
            throw new DataSourceException("Data source driver not found");
        }
        DataSource dataSource = DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driver).build();
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDatasource(dataSource);
    }


}
