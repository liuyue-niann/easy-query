package com.nn.core.wrapper;

import com.nn.core.dql.QueryExecute;
import com.nn.core.wrapper.impl.QueryWrapper;
import com.nn.entity.User;

import java.util.Collection;
import java.util.List;

/**
 * @author niann
 * @date 2024/2/2 13:32
 * @description
 **/
public interface Wrapper<E> {
      QueryWrapper<E> where();
      QueryWrapper<E> eq(String field, Object val);
      QueryWrapper<E> gt(String field,Object val);
      QueryWrapper<E> in(String field, Collection<Object> val);
      QueryWrapper<E> and();
      QueryExecute<E> build();

}
