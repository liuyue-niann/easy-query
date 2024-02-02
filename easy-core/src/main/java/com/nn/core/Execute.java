package com.nn.core;

import com.nn.config.DataSourceConfig;
import com.nn.entity.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * @author niann
 * @date 2024/2/2 10:51
 * @description sql执行器
 **/
public class Execute<E> {
    private final BaseEntity baseEntity;
    public Execute(BaseEntity baseEntity) {
        this.baseEntity = baseEntity;
    }

    public List<E> list(){
        String sql = baseEntity.getSql().toString();
        Deque<Object> fieldValue = this.baseEntity.fieldValue;
        //打印日志
        System.out.println(sql);
        System.out.println(fieldValue);
        DataSource datasource = this.baseEntity.getDatasource().getDataSource();
        try (Connection connection = datasource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            //插入占位符
            for (int i = 0; i < fieldValue.size(); i++) {
                statement.setObject(i+1,fieldValue.pop());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
