package com.nn.core.wrapper.impl;


import com.nn.core.BaseEntity;
import com.nn.core.dql.QueryExecute;
import com.nn.core.wrapper.Wrapper;

import java.util.Collection;
import java.util.List;

/**
 * @author niann
 * @date 2024/2/2 10:51
 * @description 条件构造器
 **/
public class QueryWrapper<E> implements Wrapper<E> {
    private final BaseEntity baseEntity;
    public String getSql(){
        return this.baseEntity.getSql().toString();
    }
    public QueryWrapper(BaseEntity baseEntity){
        this.baseEntity = baseEntity;
    }
    @Override
    public QueryWrapper<E> where(){
        this.baseEntity.appendSql("where");
        return new QueryWrapper<E>(this.baseEntity);
    }
    @Override
    public QueryWrapper<E> eq(String field, Object val){
        this.baseEntity.appendSql("%s = ?".formatted(field));
        this.baseEntity.getFieldValue().add(val);
        return new QueryWrapper<E>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> gt(String field, Object val) {
        this.baseEntity.appendSql("%s > ?".formatted(field));
        this.baseEntity.getFieldValue().add(val);
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> in(String field, Collection<Object> val) {
        StringBuilder sql = new StringBuilder("%s in (".formatted(field));
        for (Object o : val) {
            this.baseEntity.getFieldValue().add(o);
            sql.append("?,");
        }
        sql.delete(sql.length()-1,sql.length());
        sql.append(")");
        this.baseEntity.appendSql(sql.toString());
        return new QueryWrapper<>(this.baseEntity);
    }
    public QueryWrapper<E> in(String field, Object... val) {
        return this.in(field,List.of(val));
    }



    @Override
    public QueryWrapper<E> and(){
        this.baseEntity.appendSql("and");
        return new QueryWrapper<E>(this.baseEntity);
    }
    @Override
    public QueryExecute<E> build(){
        return new QueryExecute<E>(baseEntity);
    }



}
