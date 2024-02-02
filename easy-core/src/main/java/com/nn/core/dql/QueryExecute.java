package com.nn.core.dql;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import com.nn.annocation.Many;
import com.nn.annocation.One;
import com.nn.annocation.Table;
import com.nn.core.BaseEntity;
import com.nn.exception.EntityException;
import com.nn.exception.QueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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


    /**
     * 一对多时进行查询
     *
     * @param
     * @param entity
     * @param field
     * @param id
     * @return
     */
    private List<E> many(Class<?> entity, Field field, String id, Object value) {
        Type type = field.getGenericType();
        // 条件判断
        if (!(type instanceof ParameterizedType parameterizedType)) {
            throw new EntityException("Fields of %s class: %s should be a list , but your field type is a single object".formatted(entity.getTypeName(), field.getName()));
        }
        Type[] types = parameterizedType.getActualTypeArguments();
        String tableName = types[0].getTypeName();
        try {
            return query(field, id, value, Class.forName(tableName));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 一对一 or 一对多 查询
     *
     * @param field
     * @param id
     * @param value
     * @return
     */
    private List<E> query(Field field, String id, Object value, Class<?> clazz) {
        List<E> list = new ArrayList<>();
        ResultSet rs = null;
        Table table = clazz.getAnnotation(Table.class);
        String tableName;
        if (table == null || table.value().isBlank() || table.value().isEmpty()) {
            tableName = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
        } else {
            tableName = table.value();
        }
        try {
            tableName = tableName.substring(tableName.lastIndexOf(".") + 1);
            String sql = "select * from %s where %s = %s".formatted(tableName, id, value);
            PreparedStatement statement = this.baseEntity.getDatasource().getDataSource().getConnection().prepareStatement(sql);
            statement.execute();
            rs = statement.getResultSet();
            Field[] fields = clazz.getDeclaredFields();
            while (rs.next()) {
                E e = (E) clazz.getDeclaredConstructor().newInstance();
                for (Field var : fields) {
                    var.setAccessible(true);
                    com.nn.annocation.Field fieldAnno = field.getAnnotation(com.nn.annocation.Field.class);
                    String name;
                    if (fieldAnno != null) {
                        name = fieldAnno.value().isBlank() ? var.getName() : fieldAnno.value();
                        if (fieldAnno.required()) {
                            Object val = rs.getObject(name);
                            var.set(e, val);
                        }
                    } else {
                        name = var.getName();
                        Object val = rs.getObject(name);
                        var.set(e, val);
                    }
                }
                list.add(e);
            }
            return list;
        } catch (SQLException | IllegalAccessException | InvocationTargetException |
                 InstantiationException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 查询多行
     *
     * @return
     */
    public List<E> list() {
        List<E> list = new ArrayList<>();
        Class<?> entity = this.baseEntity.getTable();
        try (ResultSet rs = getResultSet()) {
            while (rs.next()) {
                E objectEntity = (E) entity.getDeclaredConstructor().newInstance();
                //构造实体
                Field[] fields = entity.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
//                   有@Many注解时:一对多
                    Many manyAnno = field.getAnnotation(Many.class);
                    if (manyAnno != null) {
                        String id = manyAnno.value().isBlank() ? field.getName() : manyAnno.value();
                        Object val = rs.getObject(id);
                        List<E> e = many(entity, field, id, val);
                        field.set(objectEntity, e);
                        continue;
                    }
//                    有@One注解时:一对一
                    One oneAnno = field.getAnnotation(One.class);
                    if (oneAnno != null) {
                        String id = oneAnno.value().isBlank() ? field.getName() : oneAnno.value();
                        Object val = rs.getObject(id);
                        E e = one(entity, field, id, val);
                        field.set(objectEntity, e);
                        continue;
                    }
                    com.nn.annocation.Field fieldAnno = field.getAnnotation(com.nn.annocation.Field.class);
                    String name;
                    if (fieldAnno != null) {
                        name = fieldAnno.value().isBlank() ? field.getName() : fieldAnno.value();
                        if (fieldAnno.required()) {
                            Object val = rs.getObject(name);
                            field.set(objectEntity, val);
                        }
                    } else {
                        name = field.getName();
                        Object val = rs.getObject(name);
                        field.set(objectEntity, val);
                    }
                }
                list.add(objectEntity);
            }
        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private E one(Class<?> entity, Field field, String id, Object val) {
        Type type = field.getGenericType();
        // 条件判断
        if (type instanceof ParameterizedType) {
            throw new EntityException("Fields of %s class: %s should be a object , but your field type is a single list".formatted(entity.getTypeName(), field.getName()));
        }
        String tableName = field.getType().getName();
        try {
            Class<?> clazz = Class.forName(tableName);
            List<E> list = query(field, id, val, clazz);
            if (list.size() > 1) {
                throw new QueryException("The field %s should receive 1 parameter, but the result is %s".formatted(field.getName(), list.size()));
            }
            if (list.isEmpty()) {
                return null;
            }
            return list.get(0);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询一行
     *
     * @return
     */
    public E one() {
        List<E> list = list();
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            throw new QueryException("The query result expectation is: 1 row, but the result is: %s row".formatted(list.size()));
        }

        return list.get(0);
    }


    /**
     * 返回结果集
     *
     * @return ResultSet
     */
    private ResultSet getResultSet() {
        String sql = baseEntity.getSql().toString();
        Deque<Object> fieldValue = this.baseEntity.getFieldValue();
        //TODO 打印日志
        logger.info("sql : %s".formatted(sql));
        if (fieldValue.isEmpty()) {
            logger.info("args: null");
        } else {
            StringBuilder logStr = new StringBuilder();
            for (Object o : fieldValue) {
                logStr.append(o).append(",");
            }
            logStr.delete(logStr.length() - 1, logStr.length());
            logger.info("args: %s".formatted(logStr));
        }
        DataSource datasource = this.baseEntity.getDatasource().getDataSource();
        try {
            Connection connection = datasource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            //插入占位符
            int i = 1;
            while (!fieldValue.isEmpty()) {
                Object value = fieldValue.pop();
                if (value instanceof List<?>) {
                    Object[] array = ((List<?>) value).toArray();
                    statement.setObject(i, array[0]);
                } else {
                    statement.setObject(i, value);
                }
                i++;
            }
            return statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
