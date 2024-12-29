package com.mini.project.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.project.constant.Constant;
import com.mini.project.model.Ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TicketRepository {

  private final RedisTemplate<String, String> redisTemplate;
  private final ListOperations<String, String> listOperations;
  private final ObjectMapper objectMapper;

  @Autowired
  public TicketRepository(@Qualifier(Constant.template01) RedisTemplate<String, String> redisTemplate,
      ObjectMapper objectMapper) {
    this.redisTemplate = redisTemplate;
    this.listOperations = redisTemplate.opsForList();
    this.objectMapper = objectMapper;
  }

  public void saveTicket(String userId, Ticket ticket) {
    String key = "user:" + userId + ":tickets";
    try {
      String ticketJson = objectMapper.writeValueAsString(ticket);
      listOperations.rightPush(key, ticketJson);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public List<Ticket> getAllTickets(String userId) {
    String key = "user:" + userId + ":tickets";
    List<String> ticketJsonList = listOperations.range(key, 0, -1);
    List<Ticket> tickets = new ArrayList<>();
    if (ticketJsonList != null) {
      for (String ticketJson : ticketJsonList) {
        try {
          tickets.add(objectMapper.readValue(ticketJson, Ticket.class));
        } catch (JsonProcessingException e) {
          e.printStackTrace();
        }
      }
    }
    return tickets;
  }

  public List<Ticket> getRecentTickets(String userId, int count) {
    String key = "user:" + userId + ":tickets";
    List<String> ticketJsonList = listOperations.range(key, -count, -1);
    List<Ticket> tickets = new ArrayList<>();
    if (ticketJsonList != null) {
      for (String ticketJson : ticketJsonList) {
        try {
          tickets.add(objectMapper.readValue(ticketJson, Ticket.class));
        } catch (JsonProcessingException e) {
          e.printStackTrace();
        }
      }
    }
    return tickets;
  }

  // Remove a ticket by ID
  // Not currenlty in use
  public void removeTicket(String userId, String ticketId) {
    String key = "user:" + userId + ":tickets";
    List<String> ticketJsonList = listOperations.range(key, 0, -1); // Fetch all tickets
    if (ticketJsonList != null) {
      List<String> updatedList = new ArrayList<>();
      for (String ticketJson : ticketJsonList) {
        try {
          Ticket ticket = objectMapper.readValue(ticketJson, Ticket.class);
          if (!ticket.getTicketId().equals(ticketId)) {
            updatedList.add(ticketJson);
          }
        } catch (JsonProcessingException e) {
          e.printStackTrace();
        }
      }
      redisTemplate.delete(key);
      listOperations.rightPushAll(key, updatedList);
    }
  }

}
