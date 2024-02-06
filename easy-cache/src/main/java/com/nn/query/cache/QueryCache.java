package com.nn.query.cache;

/**
 * @author niann
 * @date 2024/2/4 20:34
 * @description 添加查询缓存
 **/
public interface QueryCache {
    void save(String key, Object value);

    Object query(String key);

    void clear();
}
