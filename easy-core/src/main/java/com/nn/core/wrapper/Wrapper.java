package com.nn.core.wrapper;

import com.nn.core.dql.QueryExecute;
import com.nn.core.wrapper.impl.QueryWrapper;

import java.util.Collection;

/**
 * @author niann
 * @date 2024/2/2 13:32
 * @description 条件构造器
 **/
public interface Wrapper<E> {
    QueryWrapper<E> where();

    QueryWrapper<E> eq(String field, Object val);

    QueryWrapper<E> ne(String field, Object val);

    QueryWrapper<E> ge(String field, Object val);

    QueryWrapper<E> gt(String field, Object val);

    QueryWrapper<E> lt(String field, Object val);

    QueryWrapper<E> le(String field, Object val);

    QueryWrapper<E> in(String field, Collection<Object> val);

    QueryWrapper<E> in(String field, Object... val);

    QueryWrapper<E> and();

    QueryWrapper<E> or();

    QueryExecute<E> build();

}
