package com.mini.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.util.*;

@Service
public class IndexService {

  @Value("${PYTHON_SERVER_URL}")
  private String PYTHON_SERVER_URL;

  public List<Map<String, Object>> getHistoryData() {
    RestTemplate restTemplate = new RestTemplate();
    String url = PYTHON_SERVER_URL + "/get-data";
    ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
    return response.getBody();
  }

  public String checkPrizeGroup(List<Integer> userNumbers, String drawDate) {

    List<Map<String, Object>> historyData = getHistoryData();
    Map<String, Object> drawData = historyData.stream()
                                              .filter(draw -> draw.get("Date").equals(drawDate))
                                              .findFirst()
                                              .orElseThrow(() -> new IllegalArgumentException("Invalid draw date: " + drawDate));
    List<Integer> winningNumbers = Arrays.asList(
        (Integer) drawData.get("Winning Number 1"),
        (Integer) drawData.get("Winning Number 2"),
        (Integer) drawData.get("Winning Number 3"),
        (Integer) drawData.get("Winning Number 4"),
        (Integer) drawData.get("Winning Number 5"),
        (Integer) drawData.get("Winning Number 6"));
    int additionalNumber = (Integer) drawData.get("Supplementary Number");
    long matchingNumbers = userNumbers.stream()
                                      .filter(winningNumbers::contains)
                                      .count();
    boolean hasAdditionalNumber = userNumbers.contains(additionalNumber);
    String result;
    if (matchingNumbers == 6) {
      result = "Group 1: Jackpot (All 6 Winning Numbers). Prize Amount: " + drawData.get("Division 1 Prize");
    } else if (matchingNumbers == 5 && hasAdditionalNumber) {
      result = "Group 2: 5 Winning Numbers + Additional Number. Prize Amount: " + drawData.get("Division 2 Prize");
    } else if (matchingNumbers == 5) {
      result = "Group 3: 5 Winning Numbers. Prize Amount: " + drawData.get("Division 3 Prize");
    } else if (matchingNumbers == 4 && hasAdditionalNumber) {
      result = "Group 4: 4 Winning Numbers + Additional Number. Prize Amount: " + drawData.get("Division 4 Prize");
    } else if (matchingNumbers == 4) {
      result = "Group 5: 4 Winning Numbers. Prize Amount: " + drawData.get("Division 5 Prize");
    } else if (matchingNumbers == 3 && hasAdditionalNumber) {
      result = "Group 6: 3 Winning Numbers + Additional Number. Prize Amount: " + drawData.get("Division 6 Prize");
    } else if (matchingNumbers == 3) {
      result = "Group 7: 3 Winning Numbers. Prize Amount: " + drawData.get("Division 7 Prize");
    } else {
      result = "No Prize";
    }

    return result;
  }

}
