package com.mini.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mini.project.service.IndexService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping("")
    public String homePage(Model model) {

        List<Map<String, Object>> histData = indexService.getHistoryData();
        model.addAttribute("histData", histData);
        model.addAttribute("latestData", histData.get(0));

        return "index";
    }

    @GetMapping("/data/{date}")
    @ResponseBody
    public Map<String, Object> getDataByDate(@PathVariable("date") String date) {

        List<Map<String, Object>> histData = indexService.getHistoryData();

        return histData.stream()
                        .filter(data -> date.equals(data.get("Date")))
                        .findFirst()
                        .orElse(null);
    }

    public String checkPrizeGroup(List<Integer> userNumbers, List<Integer> winningNumbers, int additionalNumber) {
        //count matches
        long matchingNumbers = userNumbers.stream()
                .filter(winningNumbers::contains)
                .count();

        // Check matches
        boolean hasAdditionalNumber = userNumbers.contains(additionalNumber);

        // Determine the prize group
        if (matchingNumbers == 6) {
            return "Group 1: Jackpot (All 6 Winning Numbers)";
        } else if (matchingNumbers == 5 && hasAdditionalNumber) {
            return "Group 2: 5 Winning Numbers + Additional Number";
        } else if (matchingNumbers == 5) {
            return "Group 3: 5 Winning Numbers";
        } else if (matchingNumbers == 4 && hasAdditionalNumber) {
            return "Group 4: 4 Winning Numbers + Additional Number";
        } else if (matchingNumbers == 4) {
            return "Group 5: 4 Winning Numbers";
        } else if (matchingNumbers == 3 && hasAdditionalNumber) {
            return "Group 6: 3 Winning Numbers + Additional Number";
        } else if (matchingNumbers == 3) {
            return "Group 7: 3 Winning Numbers";
        } else {
            return "No Prize";
        }
    }

    @PostMapping("/check-win")
    @ResponseBody
    public Map<String, Object> checkWin(@RequestBody Map<String, Object> request) {

        List<Integer> userNumbers = (List<Integer>) request.get("numbers");
        // System.out.println(userNumbers);
        String drawDate = (String) request.get("drawDate");
        String prizeGroup = indexService.checkPrizeGroup(userNumbers, drawDate);
        Map<String, Object> response = new HashMap<>();
        response.put("prizeGroup", prizeGroup);
        return response;
    }

}
