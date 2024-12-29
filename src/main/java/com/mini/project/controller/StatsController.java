package com.mini.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

import com.mini.project.service.StatsService;



@Controller
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/stats")
    public String statsPage(Model model) {
        // Retrieve data from StatsService
        List<Map<String, Object>> numberFrequency = statsService.getNumberFrequency();
        List<Map<String, Object>> ballFrequency = statsService.getBallFrequency();
        Map<String, Map<String, Integer>> pairFrequency = statsService.getPairFrequency();
        // System.out.println(ballFrequency);
        model.addAttribute("numberFrequency", numberFrequency);
        model.addAttribute("ballFrequency", ballFrequency);
        model.addAttribute("pairFrequency", pairFrequency);

        return "stats"; // Return the name of the HTML template
    }

    @PostMapping("/get-recommendation")
    @ResponseBody
    public List<Map<String, Object>> getRecommendation(@RequestBody Map<String, Object> userInput) {
        List<Integer> numbers = (List<Integer>) userInput.get("user_input");
        System.out.println(statsService.getRecommendations(numbers));
        return statsService.getRecommendations(numbers);
    }

}
