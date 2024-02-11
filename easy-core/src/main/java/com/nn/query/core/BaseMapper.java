package com.nn.query.core;

import com.nn.query.annocation.Id;
import com.nn.query.annocation.ManyToOne;
import com.nn.query.annocation.OneToMany;
import com.nn.query.annocation.Table;
import com.nn.query.core.query.QueryExecute;
import com.nn.query.core.wrapper.impl.QueryWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

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

    public QueryWrapper<E> select() {
        this.baseEntity.setSql(new StringBuffer("select * from %s".formatted(this.baseEntity.getTableName())));
        return this.queryWrapper;
    }

    public QueryWrapper<E> update() {
        this.baseEntity.setSql(new StringBuffer("update..."));
        return this.queryWrapper;
    }


    public int insert(E e) {
        StringBuilder sql = new StringBuilder("insert into %s (".formatted(this.baseEntity.getTableName()));
        HashMap<String, Object> dbField = getDbField(e);
        for (String filed : dbField.keySet()) {
            sql.append(filed).append(",");
        }
        sql.delete(sql.length() - 1, sql.length()).append(") ").append("values (");
        for (Object value : dbField.values()) {
            if (value == null) {
                sql.append("null").append(",");
            } else {
                sql.append("?").append(",");
                this.baseEntity.getFieldValue().add(value);
            }

        }
        sql.delete(sql.length() - 1, sql.length()).append(") ");
        this.baseEntity.setSql(sql.toString());
        return queryWrapper.build().insert();
    }


    /**
     * 根据实体获取数据库字段
     *
     * @return
     */
    private HashMap<String, Object> getDbField(E table) {
        HashMap<String, Object> fieldVar = new HashMap<>();
        Field[] fields = table.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getAnnotation(Id.class) != null && field.getAnnotation(Id.class).auto() ||
                    field.getAnnotation(ManyToOne.class) != null ||
                    field.getAnnotation(OneToMany.class) != null) continue;
            try {
                String name;
                if (field.getAnnotation(com.nn.query.annocation.Field.class) == null) {
                    name = field.getName();
                } else {
                    name = field.getAnnotation(com.nn.query.annocation.Field.class).value().isBlank() ?
                            field.getName() : field.getAnnotation(com.nn.query.annocation.Field.class).value();
                }
                Object val = field.get(table);
                fieldVar.put(name, val);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return fieldVar;
    }

    public QueryWrapper<E> delete() {
        this.baseEntity.setSql("delete from %s".formatted(this.baseEntity.getTableName()));
        return this.queryWrapper;
    }

    public int deleteById(Object... ids) {
        this.baseEntity.setSql("delete from %s".formatted(this.baseEntity.getTableName()));
        this.queryWrapper.where().in(this.baseEntity.getTableId(), ids);
        return this.queryWrapper.build().del();
    }

    public List<E> list() {
        this.baseEntity.setSql(new StringBuffer("select * from %s".formatted(this.baseEntity.getTableName())));
        QueryExecute<E> execute = new QueryExecute<>(baseEntity);
        return execute.list();
    }

    public E byId(Object id) {
        StringBuffer sql = new StringBuffer("select * from %s where %s = %s ".formatted(this.baseEntity.getTableName(), this.baseEntity.getTableId(), id));
        this.baseEntity.setSql(sql);
        QueryExecute<E> execute = new QueryExecute<>(baseEntity);
        return execute.one();
    }

}
