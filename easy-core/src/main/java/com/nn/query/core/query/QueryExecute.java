package com.nn.query.core.query;

import com.nn.query.annocation.Id;
import com.nn.query.annocation.ManyToOne;
import com.nn.query.annocation.OneToMany;
import com.nn.query.annocation.Table;
import com.nn.query.cache.QueryCache;
import com.nn.query.cache.impl.QueryCacheImpl;
import com.nn.query.config.DataSourceConfig;
import com.nn.query.core.BaseEntity;
import com.nn.query.exception.EntityException;
import com.nn.query.exception.QueryException;
import org.springframework.jdbc.core.JdbcTemplate;

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
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author niann
 * @date 2024/2/2 10:51
 * @description sql执行处理器
 **/
public class QueryExecute<E> {
    private final BaseEntity baseEntity;
    private final Logger logger = Logger.getLogger("");


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
    private List<E> oneToMany(Class<?> entity, Field field, String id, Object value) {
        Type type = field.getGenericType();
        // 条件判断
        if (!(type instanceof ParameterizedType parameterizedType)) {
            throw new EntityException("Fields of %s class: %s should be a list , but your field type is a single object".formatted(entity.getTypeName(), field.getName()));
        }
        Type[] types = parameterizedType.getActualTypeArguments();
        String tableName = types[0].getTypeName();
        try {
            List<E> list = query(field, id, value, Class.forName(tableName));
            saveCache(list);
            return list;
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
        Object cacheData = queryCache();
        if (cacheData != null) {
            if (cacheData instanceof List<?>) {
                return (List<E>) cacheData;
            } else {
                return (List<E>) List.of(cacheData);
            }
        }
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
                    ManyToOne manyToOne = var.getAnnotation(ManyToOne.class);
                    OneToMany oneToMany = var.getAnnotation(OneToMany.class);
                    if (manyToOne != null || oneToMany != null) {
                        continue;
                    }
                    com.nn.query.annocation.Field fieldAnno = var.getAnnotation(com.nn.query.annocation.Field.class);
                    //TODO
                    Id idAnno = var.getAnnotation(Id.class);
                    String name;
                    if (fieldAnno != null) {
                        name = fieldAnno.value().isBlank() ? var.getName() : fieldAnno.value();
                        if (fieldAnno.required()) {
                            Object val = rs.getObject(name);
                            var.set(e, val);
                        }
                    } else if (idAnno != null) {
                        name = idAnno.value().isBlank() ? var.getName() : idAnno.value();
                        Object val = rs.getObject(name);
                        var.set(e, val);
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

    private Object queryCache() {
        QueryCache cache = new QueryCacheImpl();
        String sql = this.baseEntity.getSql().toString();
        return cache.query(sql);
    }

    private void saveCache(Object value) {
        QueryCache cache = new QueryCacheImpl();
        cache.save(this.baseEntity.getSql().toString(), value);
    }

    public List<E> page(Long pageNumber, Long limit) {
        if (pageNumber <= 0) {
            throw new QueryException("pageNumber must > 0 ！");
        }
        String id = this.baseEntity.getTableId();
        this.baseEntity.appendSql("order by %s".formatted(id));
        this.baseEntity.appendSql("offset %s limit %s".formatted(pageNumber - 1, limit));
        return list();
    }

    public List<E> page(String pageNumber, String limit) {
        return page(Long.parseLong(pageNumber), Long.parseLong(limit));
    }

    public List<E> page(Integer pageNumber, Integer limit) {
        return page(pageNumber.toString(), limit.toString());
    }

    public List<E> limit(Long limit) {
        this.baseEntity.appendSql("limit %s".formatted(limit));
        return list();
    }

    public List<E> limit(Integer limit) {
        return limit(limit.toString());
    }

    public List<E> limit(String limit) {
        return limit(Long.parseLong(limit));
    }

    /**
     * 查询多行
     *
     * @return
     */
    public List<E> list() {
        Object data = queryCache();
        if (data != null) {
            if (data instanceof List<?>) {
                return (List<E>) data;
            } else {
                return (List<E>) List.of(data);
            }
        }
        List<E> list = new ArrayList<>();
        Class<?> entity = this.baseEntity.getTable();
        try (ResultSet rs = getResultSet()) {
            while (rs.next()) {
                E objectEntity = (E) entity.getDeclaredConstructor().newInstance();
                //构造实体
                Field[] fields = entity.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    ManyToOne manyToOneAnno = field.getAnnotation(ManyToOne.class);
                    if (manyToOneAnno != null) {
                        String fieldName = manyToOneAnno.value().isBlank() ? field.getName() : manyToOneAnno.value();
                        Object val = rs.getObject(fieldName);
                        E e = manyToOne(entity, field, fieldName, val);

                        field.set(objectEntity, e);
                        continue;
                    }
                    OneToMany oneToManyAnno = field.getAnnotation(OneToMany.class);
                    if (oneToManyAnno != null) {
                        String fieldName = oneToManyAnno.value().isBlank() ? field.getName() : oneToManyAnno.value();
                        Object val = rs.getObject(fieldName);
                        List<E> e = oneToMany(entity, field, fieldName, val);
                        field.set(objectEntity, e);
                        continue;
                    }
                    com.nn.query.annocation.Field fieldAnno = field.getAnnotation(com.nn.query.annocation.Field.class);
                    Id idAnno = field.getAnnotation(Id.class);
                    String name;
                    if (fieldAnno != null) {
                        name = fieldAnno.value().isBlank() ? field.getName() : fieldAnno.value();
                        if (fieldAnno.required()) {
                            Object val = rs.getObject(name);
                            field.set(objectEntity, val);
                        }
                    } else if (idAnno != null) {
                        name = idAnno.value().isBlank() ? field.getName() : idAnno.value();
                        Object val = rs.getObject(name);
                        field.set(objectEntity, val);
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
        //添加缓存
        saveCache(list);
        return list;
    }

    private E manyToOne(Class<?> entity, Field field, String fieldName, Object val) {
        Type type = field.getGenericType();
        // 条件判断
        if (type instanceof ParameterizedType) {
            throw new EntityException("Fields of %s class: %s should be a object , but your field type is a single list".formatted(entity.getTypeName(), field.getName()));
        }
        String tableName = field.getType().getName();
        try {
            Class<?> clazz = Class.forName(tableName);
            List<E> list = query(field, fieldName, val, clazz);
            if (list.size() > 1) {
                throw new QueryException("The field %s should receive 1 parameter, but the result is %s".formatted(field.getName(), list.size()));
            }
            if (list.isEmpty()) {
                return null;
            }
            E r = list.get(0);
            saveCache(r);
            return r;
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
        //添加缓存
        E r = list.get(0);
        saveCache(r);
        return r;
    }

    /**
     * 返回结果集
     *
     * @return ResultSet
     */
    private ResultSet getResultSet() {
        String sql = baseEntity.getSql().toString();
        Deque<Object> fieldValue = this.baseEntity.getFieldValue();
        // 打印日志
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

    public int insert() {
        String sql = this.baseEntity.getSql().toString();
        Object[] value = this.baseEntity.getFieldValue().toArray();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new DataSourceConfig().getDataSource());
        StringBuilder argsLog = new StringBuilder();
        for (Object o : value) {
            argsLog.append(o).append(",");
        }
        if (!argsLog.isEmpty()) {
            argsLog.delete(argsLog.length() - 1, argsLog.length());
        } else {
            argsLog.append("null");
        }
        logger.info("==> sql: %s".formatted(sql));
        logger.info("==> args: %s".formatted(argsLog));
        int i = jdbcTemplate.update(sql, value);
        QueryCache cache = new QueryCacheImpl();
        cache.clear();
        return i;
    }

    public int del() {
        String sql = this.baseEntity.getSql().toString();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new DataSourceConfig().getDataSource());
        Object[] args = this.baseEntity.getFieldValue().toArray();
        logger.info("==> sql: %s".formatted(sql));
        logger.info("==> args: " + Arrays.toString(args));
        int i = jdbcTemplate.update(sql, args);
        QueryCache cache = new QueryCacheImpl();
        cache.clear();
        return i;
    }
}
