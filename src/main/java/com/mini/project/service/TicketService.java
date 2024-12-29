package com.mini.project.service;

import com.mini.project.model.Ticket;
import com.mini.project.repo.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class TicketService {

  @Autowired
  private TicketRepository ticketRepository;

  @Value("${PYTHON_SERVER_URL}")
  private String PYTHON_SERVER_URL;

  public List<Map<String, Object>> getHistoryData() {
    RestTemplate restTemplate = new RestTemplate();
    String url = PYTHON_SERVER_URL + "/get-data";
    ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
    return response.getBody();
  }

  public double checkPrizeGroup(List<Integer> userNumbers, String drawDate) {
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
    // System.out.println(winningNumbers);
    int additionalNumber = (Integer) drawData.get("Supplementary Number");
    long matchingNumbers = userNumbers.stream()
                                        .filter(winningNumbers::contains)
                                        .count();
    boolean hasAdditionalNumber = userNumbers.contains(additionalNumber);
    String result;
    if (matchingNumbers == 6) {
      result = "" + drawData.get("Division 1 Prize");
    } else if (matchingNumbers == 5 && hasAdditionalNumber) {
      result = "" + drawData.get("Division 2 Prize");
    } else if (matchingNumbers == 5) {
      result = "" + drawData.get("Division 3 Prize");
    } else if (matchingNumbers == 4 && hasAdditionalNumber) {
      result = "" + drawData.get("Division 4 Prize");
    } else if (matchingNumbers == 4) {
      result = "" + drawData.get("Division 5 Prize");
    } else if (matchingNumbers == 3 && hasAdditionalNumber) {
      result = "" + drawData.get("Division 6 Prize");
    } else if (matchingNumbers == 3) {
      result = "" + drawData.get("Division 7 Prize");
    } else {
      result = "0.0";
    }
    return Double.parseDouble(result.replace("$", "").replace(",", ""));
  }

  public void processTicketPrize(Ticket ticket) {
    List<Map<String, Object>> historyData = getHistoryData();
    Map<String, Object> nextDraw = historyData.stream()
        .filter(draw -> LocalDate.parse((String) draw.get("Date")).isAfter(ticket.getPurchaseDate()))
        .reduce((first, second) -> second) // Get the last match
        .orElse(null);
    // System.out.println(ticket.getNumbers());
    if (nextDraw == null) {
      ticket.setPrizeAmount(0.0);
    } else {
      String drawDate = (String) nextDraw.get("Date");
      double prize = checkPrizeGroup(ticket.getNumbers(), drawDate);
      ticket.setPrizeAmount(prize);
    }
  }

  public void addTicketWithPrize(String userId, Ticket ticket) {
    if (userId == null || ticket == null) {
      throw new IllegalArgumentException("User ID and ticket are required.");
    }
    processTicketPrize(ticket);
    ticketRepository.saveTicket(userId, ticket);
  }

  // Add a ticket for a user
  public void addTicket(String userId, Ticket ticket) {
    if (userId == null || ticket == null) {
      throw new IllegalArgumentException("User ID and ticket are required.");
    }
    ticketRepository.saveTicket(userId, ticket);
  }

  // Get all tickets for a user
  public List<Ticket> getAllTickets(String userId) {
    if (userId == null) {
      throw new IllegalArgumentException("User ID is required.");
    }
    return ticketRepository.getAllTickets(userId);
  }

  // Get recent N tickets for a user
  public List<Ticket> getRecentTickets(String userId, int count) {
    if (userId == null || count <= 0) {
      throw new IllegalArgumentException("User ID and a valid count are required.");
    }
    return ticketRepository.getRecentTickets(userId, count);
  }

  // Remove a ticket by ID for a user
  public void removeTicket(String userId, String ticketId) {
    if (userId == null || ticketId == null) {
      throw new IllegalArgumentException("User ID and ticket ID are required.");
    }
    ticketRepository.removeTicket(userId, ticketId);
  }
}
