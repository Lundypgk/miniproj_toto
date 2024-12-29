package com.mini.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.*;

@Service
public class StatsService {

    @Value("${PYTHON_SERVER_URL}")
    private String PYTHON_SERVER_URL;
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Map<String, Object>> getNumberFrequency() {
        String url = PYTHON_SERVER_URL + "/get-num-freq";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return response.getBody();
    }

    public List<Map<String, Object>> getBallFrequency() {
        String url = PYTHON_SERVER_URL + "/get-ball-freq";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return response.getBody();
    }

    public Map<String, Map<String, Integer>> getPairFrequency() {
        String url = PYTHON_SERVER_URL + "/get-pair-freq";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return response.getBody();
    }

    public List<Map<String, Object>> getRecommendations(List<Integer> userInput) {
        String url = PYTHON_SERVER_URL + "/get-recommendation";
        Map<String, Object> requestBody = Map.of("user_input", userInput);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        List<List<Object>> rawRecommendations = (List<List<Object>>) response.getBody().get("recommendation");
        List<Map<String, Object>> recommendations = new ArrayList<>();

        for (List<Object> recommendation : rawRecommendations) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("number", recommendation.get(0));
            entry.put("strength", recommendation.get(1));
            recommendations.add(entry);
        }
        // System.out.println(recommendations);
        return recommendations;
    }

}
