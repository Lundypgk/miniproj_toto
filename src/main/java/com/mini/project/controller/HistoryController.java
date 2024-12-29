package com.mini.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mini.project.service.HistoryService;
import java.util.*;

@Controller
public class HistoryController {

    @Autowired
    public HistoryService historyService;

    @GetMapping("/history")
    public String statsPage(@RequestParam(defaultValue = "0") int page, Model model) {

        int pageSize = 30;
        List<Map<String, Object>> historyData = historyService.getPaginatedHistoryData(page, pageSize);
        int totalPages = historyService.getTotalPages(pageSize);

        model.addAttribute("historyData", historyData);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "history";
    }
}
