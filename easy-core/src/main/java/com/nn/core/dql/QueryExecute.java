package com.nn.core.dql;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import com.nn.core.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        List<E> list = new ArrayList<>();
        try (ResultSet rs = getResultSet()){
            Class<?> entity = this.baseEntity.getTable();
            while (rs.next()){
                int columnCount = rs.getMetaData().getColumnCount();
                E objectEntity = (E) entity.getDeclaredConstructor().newInstance();
                //构造实体
                Field[] fields = entity.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    com.nn.annocation.Field fieldAnno = field.getAnnotation(com.nn.annocation.Field.class);
                    String name;
                    if (fieldAnno!=null){
                        name = fieldAnno.value().isBlank()?field.getName():fieldAnno.value();
                        if (fieldAnno.required()){
                            Object val = rs.getObject(name);
                            field.set(objectEntity,val);
                        }
                    }else {
                        name = field.getName();
                        Object val = rs.getObject(name);
                        field.set(objectEntity,val);
                    }

                }
                list.add(objectEntity);
            }
            return list;
        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }




    /**
     * 返回结果集
     * @return ResultSet
     */
    private ResultSet getResultSet(){
        String sql = baseEntity.getSql().toString();
        Deque<Object> fieldValue = this.baseEntity.fieldValue;
        //TODO 打印日志
        logger.info("sql : %s".formatted(sql));
        logger.info("args: %s".formatted(fieldValue));
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
