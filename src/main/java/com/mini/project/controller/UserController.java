package com.mini.project.controller;

import com.mini.project.model.Ticket;
import com.mini.project.model.User;
import com.mini.project.service.TicketService;
import com.mini.project.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private TicketService ticketService;

  @GetMapping
  public String userPage(@RequestParam(required = false) String userId, Model model) {

    if (userId != null) {
      User user = userService.getUserById(userId);
      List<Ticket> tickets = ticketService.getAllTickets(userId);

      model.addAttribute("user", user);
      model.addAttribute("tickets", tickets);
    }
    return "user";
  }

  @PostMapping("/signup")
  public String signUpUser(@RequestParam String username, @RequestParam String password, Model model) {

    String userId = "user:" + username;
    if (userService.usernameExists(username)) {
      model.addAttribute("error", "Username already exists. Please choose a different one.");
      return "user";
    }

    User user = new User(userId, username, password);
    userService.addUser(user);
    model.addAttribute("user", user);
    return "redirect:/user?userId=" + user.getUserId();
  }

  @PostMapping("/login")
  public String loginUser(@RequestParam String username, @RequestParam String password, Model model) {

    String userId = "user:" + username;
    User user = userService.getUserById(userId);

    if (user != null && user.getPassword().equals(password)) {
      // login succes
      model.addAttribute("user", user);
      return "redirect:/user?userId=" + userId;
    } else {
      // login failed
      model.addAttribute("loginerror", "Invalid username or password");
      return "user";
    }
  }

  @PostMapping("/{userId}/create-ticket")
  public String createTicket(@PathVariable String userId,
      @RequestParam String betType,
      @RequestParam List<Integer> numbers,
      @RequestParam String purchaseDate,
      Model model) {

    LocalDate parsedDate = LocalDate.parse(purchaseDate);
    double betAmount = getBetAmountByType(betType);
    Ticket ticket = new Ticket(betType, parsedDate, numbers, betAmount);
    ticketService.addTicketWithPrize(userId, ticket);
    model.addAttribute("message", "Ticket created and prize calculated successfully.");
    return "redirect:/user?userId=" + userId;
  }

  private double getBetAmountByType(String betType) {
    switch (betType) {
      case "Ordinary":
        return 1.0;
      case "System 7":
        return 7.0;
      case "System 8":
        return 28.0;
      case "System 9":
        return 84.0;
      case "System 10":
        return 210.0;
      case "System 11":
        return 462.0;
      case "System 12":
        return 924.0;
      default:
        throw new IllegalArgumentException("Invalid Bet Type: " + betType);
    }
  }
}
