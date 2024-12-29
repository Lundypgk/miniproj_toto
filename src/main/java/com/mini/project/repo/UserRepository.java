package com.mini.project.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.mini.project.constant.Constant;

@Repository
public class UserRepository {

    private final HashOperations<String, String, String> hashOperations;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserRepository(@Qualifier(Constant.template02) RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.hashOperations = redisTemplate.opsForHash();
        this.objectMapper = objectMapper;
    }

    public void saveUser(User user) {
        String key = "user:" + user.getUserId();
        try {
            String userJson = objectMapper.writeValueAsString(user);
            hashOperations.put(key, "info", userJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public User getUserById(String userId) {
        String key = "user:" + userId;
        String userJson = hashOperations.get(key, "info");
        try {
            return userJson != null ? objectMapper.readValue(userJson, User.class) : null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
