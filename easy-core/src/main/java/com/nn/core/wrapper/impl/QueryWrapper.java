package com.nn.core.wrapper.impl;


import com.nn.core.BaseEntity;
import com.nn.core.dql.QueryExecute;
import com.nn.core.wrapper.Wrapper;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * @author niann
 * @date 2024/2/2 10:51
 * @description 条件构造器实现
 **/
public class QueryWrapper<E> implements Wrapper<E> {
    private final BaseEntity baseEntity;

    public QueryWrapper(BaseEntity baseEntity) {
        this.baseEntity = baseEntity;
    }

    public String getSql() {
        return this.baseEntity.getSql().toString();
    }

    @Override
    public QueryWrapper<E> where() {
        this.baseEntity.appendSql("where");
        return new QueryWrapper<E>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> limit(Object limit) {
        this.baseEntity.appendSql("limit %s".formatted(limit));
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> eq(String field, Object val) {
        this.baseEntity.appendSql("%s = ?".formatted(field));
        this.baseEntity.getFieldValue().add(val);
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> ne(String field, Object val) {
        this.baseEntity.appendSql("%s != ?".formatted(field));
        this.baseEntity.getFieldValue().add(val);
        return new QueryWrapper<E>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> ge(String field, Object val) {
        this.baseEntity.appendSql("%s >= ?".formatted(field));
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
    public QueryWrapper<E> lt(String field, Object val) {
        this.baseEntity.appendSql("%s < ?".formatted(field));
        this.baseEntity.getFieldValue().add(val);
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> le(String field, Object val) {
        this.baseEntity.appendSql("%s <= ?".formatted(field));
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
        sql.delete(sql.length() - 1, sql.length());
        sql.append(")");
        this.baseEntity.appendSql(sql.toString());
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> in(String field, Object... val) {
        return this.in(field, List.of(val));
    }


    @Override
    public QueryWrapper<E> and() {
        this.baseEntity.appendSql("and");
        return new QueryWrapper<E>(this.baseEntity);
    }

    @Override
    public QueryWrapper<E> or() {
        this.baseEntity.appendSql("or");
        return new QueryWrapper<E>(this.baseEntity);
    }


    public QueryWrapper<E> on(String args) {
        this.baseEntity.appendSql("on %s".formatted(args));
        return new QueryWrapper<>(this.baseEntity);
    }

    @Override
    public QueryExecute<E> build() {
        return new QueryExecute<>(baseEntity);
    }


}
