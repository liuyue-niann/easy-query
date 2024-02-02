package com.nn.core.wrapper.impl;


import com.nn.core.BaseEntity;
import com.nn.core.dql.QueryExecute;
import com.nn.core.wrapper.Wrapper;

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
        this.baseEntity.fieldValue.add(val);
        return new QueryWrapper<E>(this.baseEntity);
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
