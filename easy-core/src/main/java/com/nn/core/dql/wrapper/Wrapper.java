package com.nn.core.dql.wrapper;

import com.nn.core.dql.Execute;
import com.nn.core.dql.wrapper.impl.QueryWrapper;

/**
 * @author niann
 * @date 2024/2/2 13:32
 * @description
 **/
public interface Wrapper<E> {
      QueryWrapper<E> where();
      QueryWrapper<E> eq(String field, Object val);
      QueryWrapper<E> and();
      Execute<E> build();
}
