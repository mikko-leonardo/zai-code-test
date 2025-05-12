package com.zai.zaicodetest.reportcache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class CacheStore<T> {

    private final Cache<String, T> cache;

    public CacheStore() {
        cache = CacheBuilder.newBuilder()
                .build();
    }

    public T get(String key) {
        return cache.getIfPresent(key);
    }

    public void put(String key, T value) {
        if(key != null && value != null) {
            cache.put(key, value);
        }
    }

    public void invalidate(String key) {
        if (key != null) {
            cache.invalidate(key);
        }
    }

}
