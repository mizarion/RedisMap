package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

class RedisMapTest {

    private final RedisMap redisMap = new RedisMap();

    @BeforeEach
    public void setUp() {
        redisMap.clear();
    }

    @Test
    void isEmpty() {
        Assertions.assertTrue(redisMap.isEmpty());
        redisMap.put("key", "value");
        Assertions.assertFalse(redisMap.isEmpty());
    }

    @Test
    void containsKey() {
        Assertions.assertFalse(redisMap.containsKey("key"));
        redisMap.put("key", "value");
        Assertions.assertTrue(redisMap.containsKey("key"));
    }

    @Test
    void containsValue() {
        Assertions.assertFalse(redisMap.containsValue("value"));
        redisMap.put("key", "value");
        Assertions.assertTrue(redisMap.containsValue("value"));
    }

    @Test
    void get() {
        Assertions.assertNull(redisMap.get("testget"));
        redisMap.put("testget", "value");
        Assertions.assertNotNull(redisMap.get("testget"));
    }

    @Test
    void put() {
        Assertions.assertEquals(0, redisMap.size());
        redisMap.put("testput", "value");
        Assertions.assertEquals(1, redisMap.size());
    }

    @Test
    void remove() {
        Assertions.assertEquals(0, redisMap.size());
        redisMap.put("testremove", "value");
        Assertions.assertEquals(1, redisMap.size());
        redisMap.remove("testremove");
        Assertions.assertEquals(0, redisMap.size());
    }

    @Test
    void putAll() {
        Assertions.assertEquals(0, redisMap.size());
        Map<String, String> map = Map.of("key", "value");
        redisMap.putAll(map);
        Assertions.assertEquals(1, redisMap.size());
    }

    @SuppressWarnings("all")
    @Test
    void clear() {
        redisMap.put("key", "value");
        Assertions.assertFalse(redisMap.isEmpty());
        redisMap.clear();
        Assertions.assertTrue(redisMap.isEmpty());
    }

    @SuppressWarnings("all")
    @Test
    void clearEmptyMap() {
        Assertions.assertTrue(redisMap.isEmpty());
        redisMap.clear();
        Assertions.assertTrue(redisMap.isEmpty());
    }

    @Test
    void keySet() {
        Assertions.assertEquals(0, redisMap.keySet().size());
        redisMap.put("key", "value");
        Assertions.assertEquals(1, redisMap.keySet().size());
    }
}