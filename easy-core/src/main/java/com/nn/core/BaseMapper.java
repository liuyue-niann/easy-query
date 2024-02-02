package com.nn.core;

import com.nn.annocation.Table;
import com.nn.core.wrapper.impl.QueryWrapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BaseMapper<E> {

    private final BaseEntity baseEntity;

    private final QueryWrapper<E> queryWrapper;

    public BaseMapper(){
        this.baseEntity = new BaseEntity();
        getTable();
        this.queryWrapper = new QueryWrapper<>(baseEntity);
    }

    private void getTable(){
        Class<?> clazz = this.getClass();
        Type type = clazz.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        try {
            String tableName =types[0].getTypeName();
            Class<?> table = Class.forName(tableName);
            String tableAnnoVal = table.getAnnotation(Table.class)==null? "":table.getAnnotation(Table.class).value();
            if (tableAnnoVal.isBlank() || tableAnnoVal.isEmpty()){
                tableName = tableName.substring(tableName.lastIndexOf(".")+1);
            }else {
                tableName = tableAnnoVal;
            }
            this.baseEntity.setTableName(tableName);
            this.baseEntity.setTable(table);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public QueryWrapper<E> select(){
        this.baseEntity.setSql(new StringBuffer("select * from %s".formatted(this.baseEntity.getTableName())));
        return this.queryWrapper;
    }
    public QueryWrapper<E> update(){
        this.baseEntity.setSql(new StringBuffer("update..."));
        return this.queryWrapper;
    }
    public QueryWrapper<E> insert(){
        this.baseEntity.setSql(new StringBuffer("insert..."));
        return this.queryWrapper;
    }
    public QueryWrapper<E> delete(){
        this.baseEntity.setSql(new StringBuffer("delete..."));
        return this.queryWrapper;
    }




}
