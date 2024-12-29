package com.mini.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.util.*;

@Service
public class HistoryService {

    @Value("${PYTHON_SERVER_URL}")
    private String PYTHON_SERVER_URL;

    public List<Map<String, Object>> getHistoryData() {
        RestTemplate restTemplate = new RestTemplate();
        String url = PYTHON_SERVER_URL + "/get-data";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return response.getBody();
    }

    public List<Map<String, Object>> getPaginatedHistoryData(int page, int size) {
        List<Map<String, Object>> allData = getHistoryData();
        int start = Math.min(page * size, allData.size());
        int end = Math.min(start + size, allData.size());
        return allData.subList(start, end);
    }

    public int getTotalPages(int size) {
        List<Map<String, Object>> allData = getHistoryData();
        return (int) Math.ceil((double) allData.size() / size);
    }
}
