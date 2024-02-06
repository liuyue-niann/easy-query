package com.nn.query.core;

import com.nn.query.annocation.Id;
import com.nn.query.annocation.Table;
import com.nn.query.core.dql.QueryExecute;
import com.nn.query.core.wrapper.impl.QueryWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

public class BaseMapper<E> {

    private final BaseEntity baseEntity;

    private final QueryWrapper<E> queryWrapper;

    public BaseMapper() {
        this.baseEntity = new BaseEntity();
        setTable();
        setId();
        this.queryWrapper = new QueryWrapper<>(baseEntity);
    }

    private void setId() {
        Class<?> clazz = this.baseEntity.getTable();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Id idAnno = field.getAnnotation(Id.class);
            if (idAnno != null) {
                if (idAnno.value().isEmpty() || idAnno.value().isBlank()) {
                    this.baseEntity.setTableId(field.getName());
                } else {
                    this.baseEntity.setTableId(idAnno.value());
                }
                return;
            }
        }
        this.baseEntity.setTableId("id");
    }

    private void setTable() {
        Class<?> clazz = this.getClass();
        Type type = clazz.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        try {
            String tableName = types[0].getTypeName();
            Class<?> table = Class.forName(tableName);
            String tableAnnoVal = table.getAnnotation(Table.class) == null ? "" : table.getAnnotation(Table.class).value();
            if (tableAnnoVal.isBlank() || tableAnnoVal.isEmpty()) {
                tableName = tableName.substring(tableName.lastIndexOf(".") + 1);
            } else {
                tableName = tableAnnoVal;
            }
            this.baseEntity.setTableName(tableName);
            this.baseEntity.setTable(table);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public final QueryWrapper<E> select() {
        this.baseEntity.setSql(new StringBuffer("select * from %s".formatted(this.baseEntity.getTableName())));
        return this.queryWrapper;
    }

    public final QueryWrapper<E> update() {
        this.baseEntity.setSql(new StringBuffer("update..."));
        return this.queryWrapper;
    }

    public final QueryWrapper<E> insert() throws SQLException {
        this.baseEntity.setSql(new StringBuffer("insert..."));
        return this.queryWrapper;
    }

    public final QueryWrapper<E> delete() {
        this.baseEntity.setSql(new StringBuffer("delete..."));
        return this.queryWrapper;
    }

    public List<E> list() {
        this.baseEntity.setSql(new StringBuffer("select * from %s".formatted(this.baseEntity.getTableName())));
        QueryExecute<E> execute = new QueryExecute<>(baseEntity);
        return execute.list();
    }

    public final E byId(Object id) {
        StringBuffer sql = new StringBuffer("select * from %s where %s = %s ".formatted(this.baseEntity.getTableName(), this.baseEntity.getTableId(), id));
        this.baseEntity.setSql(sql);
        QueryExecute<E> execute = new QueryExecute<>(baseEntity);
        return execute.one();
    }

}
