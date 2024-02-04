package com.query.nn.core.wrapper;

import com.query.nn.core.dql.QueryExecute;
import com.query.nn.core.wrapper.impl.QueryWrapper;

import java.util.Collection;

/**
 * @author niann
 * @date 2024/2/2 13:32
 * @description 条件构造器
 **/
public interface Wrapper<E> {
    QueryWrapper<E> where();

    QueryWrapper<E> limit(Object limit);

    QueryWrapper<E> join(Class<?> clazz);

    QueryWrapper<E> eq(String field, Object val);

    QueryWrapper<E> eq(boolean bool, String field, Object val);

    QueryWrapper<E> ne(String field, Object val);

    QueryWrapper<E> ne(boolean bool, String field, Object val);

    QueryWrapper<E> ge(String field, Object val);

    QueryWrapper<E> ge(boolean bool, String field, Object val);

    QueryWrapper<E> gt(String field, Object val);

    QueryWrapper<E> gt(boolean bool, String field, Object val);

    QueryWrapper<E> lt(String field, Object val);

    QueryWrapper<E> lt(boolean bool, String field, Object val);

    QueryWrapper<E> le(String field, Object val);

    QueryWrapper<E> le(boolean bool, String field, Object val);

    QueryWrapper<E> in(String field, Collection<Object> val);


    QueryWrapper<E> in(String field, Object... val);

    QueryWrapper<E> in(boolean bool, String field, Object... val);

    QueryWrapper<E> in(boolean bool, String field, Object val);

    QueryWrapper<E> in(boolean bool, String field, Collection<Object> val);

    QueryWrapper<E> and();

    QueryWrapper<E> or();

    QueryWrapper<E> like(String field, Object var);

    QueryWrapper<E> like(boolean bool, String field, Object var);

    QueryWrapper<E> leftLike(String field, Object var);

    QueryWrapper<E> leftLike(boolean bool, String field, Object var);

    QueryWrapper<E> rightLike(String field, Object var);

    QueryWrapper<E> rightLike(boolean bool, String field, Object var);

    QueryWrapper<E> order(Object... field);

    QueryWrapper<E> orderDesc(Object... field);

    QueryExecute<E> build();

}
