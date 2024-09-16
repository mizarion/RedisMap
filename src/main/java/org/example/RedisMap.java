package org.example;

import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.stream.Collectors;

public class RedisMap implements Map<String, String> {


    private final Jedis jedis;
    private final String keyPrefix;

    public RedisMap() {
        this.jedis = new Jedis();
        this.keyPrefix = "prefix";
    }

    public RedisMap(String RedisKeyPrefix) {
        this.jedis = new Jedis();
        this.keyPrefix = RedisKeyPrefix;
    }

    public RedisMap(Jedis jedis, String RedisKeyPrefix) {
        this.jedis = jedis;
        this.keyPrefix = RedisKeyPrefix;
    }

    private String getKeyPrefix(String key) {
        return keyPrefix + ":" + key;
    }

    @Override
    public int size() {
        return jedis.keys(keyPrefix + ":*").size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key instanceof String str) {
            return jedis.exists(getKeyPrefix(str));
        }
        throw new IllegalArgumentException("Key must be a String");
    }

    @Override
    public boolean containsValue(Object value) {
        if (value instanceof String str) {
            return jedis.keys(keyPrefix + ":*").stream()
                    .map(jedis::get)
                    .toList()
                    .contains(str);
        }
        throw new IllegalArgumentException("Value must be a String");
    }

    @Override
    public String get(Object key) {
        if (key instanceof String str) {
            return jedis.get(getKeyPrefix(str));
        }
        throw new IllegalArgumentException("Key must be a String");
    }

    @Override
    public String put(String key, String value) {
        return jedis.set(getKeyPrefix(key), value);
    }

    @Override
    public String remove(Object key) {
        if (key instanceof String str) {
            String oldValue = jedis.get(getKeyPrefix(str));
            jedis.del(getKeyPrefix(str));
            return oldValue;
        }
        throw new IllegalArgumentException("Key must be a String");
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        Set<String> keys = jedis.keys(keyPrefix + ":*");
        for (String key : keys) {
            jedis.del(key);
        }
    }

    @Override
    public Set<String> keySet() {
        return jedis.keys(keyPrefix + ":*").stream()
                .map(keyPrefix -> keyPrefix.replace(keyPrefix + ":", ""))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<String> values() {
        return jedis.keys(keyPrefix + ":*").stream()
                .map(jedis::get)
                .toList();
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        Set<String> keys = jedis.keys(keyPrefix + ":*");
        Set<Entry<String, String>> entrySet = new HashSet<>();
        for (String key : keys) {
            String actualKey = key.replace(keyPrefix + ":", "");
            entrySet.add(new AbstractMap.SimpleEntry<>(actualKey, jedis.get(key)));
        }
        return entrySet;
    }
}