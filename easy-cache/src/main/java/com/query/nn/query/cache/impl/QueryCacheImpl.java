package com.query.nn.query.cache.impl;

import com.query.nn.query.cache.QueryCache;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;

/**
 * @author niann
 * @date 2024/2/4 20:34
 * @description 缓存实现类
 **/
public class QueryCacheImpl implements QueryCache {
    private final static HashMap<String, Object> cache = new HashMap<>();
    private final Deque<String> keyContinues = new ArrayDeque<>();

    @Override
    public void save(String key, Object value) {
        if (!keyContinues.isEmpty() && keyContinues.size() < 10) {
            String var = keyContinues.pop();
            cache.remove(var);
        }
        cache.put(key, value);
        this.keyContinues.add(key);
    }

    @Override
    public Object query(String key) {
        return cache.get(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
