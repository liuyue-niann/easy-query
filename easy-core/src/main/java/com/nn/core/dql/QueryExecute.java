package com.nn.core.dql;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import com.nn.core.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Deque;
import java.util.List;

/**
 * @author niann
 * @date 2024/2/2 10:51
 * @description sql执行处理器
 **/
public class QueryExecute<E> {
    private final BaseEntity baseEntity;
    private final Logger logger = LoggerFactory.getLogger(MysqlxPrepare.Execute.class);
    public QueryExecute(BaseEntity baseEntity) {
        this.baseEntity = baseEntity;
    }

    public List<E> list(){
        try (ResultSet rs = getResultSet()){
            while (rs.next()){
                System.out.println(rs.getObject(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    /**
     * 获取结果集
     * @return
     */
    private ResultSet getResultSet(){
        String sql = baseEntity.getSql().toString();
        Deque<Object> fieldValue = this.baseEntity.fieldValue;
        //TODO 打印日志
        System.out.println(sql);
        System.out.println(fieldValue);
        DataSource datasource = this.baseEntity.getDatasource().getDataSource();
        try {
            Connection connection = datasource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            //插入占位符
            int i =1;
            while (!fieldValue.isEmpty()){
                statement.setObject(i,fieldValue.pop());
                i++;
            }
            return statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
