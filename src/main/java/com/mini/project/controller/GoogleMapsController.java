package com.mini.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mini.project.service.GoogleMapsService;

import java.util.List;
import java.util.Map;

@Controller
public class GoogleMapsController {

    @Autowired
    private GoogleMapsService googleMapsService;

    @GetMapping("/location")
    public String getPlaces(Model model) {
        // List<Map<String,Object>> storage =
        // googleMapsService.getOutletsWithCoordinates();
        List<Map<String, String>> storage = googleMapsService.getOutletsWithCoordinates();
        model.addAttribute("places", storage);
        // System.out.println(storage);
        return "location";

    }

}
