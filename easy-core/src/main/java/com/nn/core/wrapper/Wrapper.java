package com.nn.core.wrapper;

import com.nn.core.dql.QueryExecute;
import com.nn.core.wrapper.impl.QueryWrapper;

/**
 * @author niann
 * @date 2024/2/2 13:32
 * @description
 **/
public interface Wrapper<E> {
      QueryWrapper<E> where();
      QueryWrapper<E> eq(String field, Object val);
      QueryWrapper<E> and();
      QueryExecute<E> build();
}
