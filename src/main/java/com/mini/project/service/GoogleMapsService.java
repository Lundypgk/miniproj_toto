package com.mini.project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoogleMapsService {

    @Value("${google.api.key}")
    private String apiKey;

    @Value("${PYTHON_SERVER_URL}")
    private String PYTHON_SERVER_URL;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String PLACES_API_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json";
    private static final String GEOCODE_API_URL = "https://maps.googleapis.com/maps/api/geocode/json";


    public List<Map<String, Object>> getOutletsWithCoordinates() {
    // public List<Map<String, String>> getOutletsWithCoordinates() {
        String url = PYTHON_SERVER_URL + "/get-outlet";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        List<Map<String, String>> outlets = response.getBody();
        // return outlets;
        return outlets.stream().map(this::addCoordinates).toList();
    }

    private Map<String, Object> addCoordinates(Map<String, String> outlet) {
        String address = outlet.get("Postal Code");
        String geocodeUrl = String.format("%s?address=%s&key=%s", GEOCODE_API_URL, address.replace(" ", "+"), apiKey);

        try {
            ResponseEntity<Map> geocodeResponse = restTemplate.getForEntity(geocodeUrl, Map.class);
            Map<String, Object> geocodeResult = (Map<String, Object>) ((List<?>) geocodeResponse.getBody().get("results")).get(0);
            Map<String, Object> location = (Map<String, Object>) ((Map<String, Object>) geocodeResult.get("geometry")).get("location");

            outlet.put("Latitude", location.get("lat").toString());
            outlet.put("Longitude", location.get("lng").toString());
        } catch (Exception e) {
            // Handle errors or log them as needed
            outlet.put("Latitude", "N/A");
            outlet.put("Longitude", "N/A");
        }

        return new HashMap<>(outlet);
    }

    public List<Map<String, Object>> searchPlaces(String query) {
        try {
            // Build the API URL
            String url = UriComponentsBuilder.fromHttpUrl(PLACES_API_URL)
                    .queryParam("query", query)
                    .queryParam("key", apiKey)
                    .toUriString();

            // Make the API request
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            // Extract and return the results
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");
            return results != null ? results : Collections.emptyList();

        } catch (Exception e) {
            // Log and return an empty list on error
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
