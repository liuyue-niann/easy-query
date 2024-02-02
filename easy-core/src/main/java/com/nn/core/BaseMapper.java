package com.nn.core;

import com.nn.entity.User;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class BaseMapper<E> {

    private final BaseEntity baseEntity;

    private final Wrapper<E> wrapper;

    public BaseMapper(){
        this.baseEntity = new BaseEntity();
        getTable();
        this.wrapper = new Wrapper<>(baseEntity);
    }

    private void getTable(){
        Class<?> clazz = this.getClass();
        Type type = clazz.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        try {
            String tableName =types[0].getTypeName();
            Class<?> table = Class.forName(tableName);
            this.baseEntity.setTableName(tableName);
            this.baseEntity.setTable(table);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public Wrapper<E> select(){
        this.baseEntity.setSql(new StringBuffer("select * from %s".formatted(this.baseEntity.getTableName())));
        return this.wrapper;
    }
    public Wrapper<E> update(){
        this.baseEntity.setSql(new StringBuffer("update..."));
        return this.wrapper;
    }
    public Wrapper<E> insert(){
        this.baseEntity.setSql(new StringBuffer("insert..."));
        return this.wrapper;
    }
    public Wrapper<E> delete(){
        this.baseEntity.setSql(new StringBuffer("delete..."));
        return this.wrapper;
    }




}
