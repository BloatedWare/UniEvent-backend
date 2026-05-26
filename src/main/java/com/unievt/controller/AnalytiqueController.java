package com.unievt.controller;

import com.unievt.dto.DashboardKpiDTO;
import com.unievt.service.AnalytiqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnalytiqueController {

    private final AnalytiqueService analytiqueService;

    // GET /analytique/dashboard — tableau de bord global avec tous les KPIs
    @GetMapping("/analytique/dashboard")
    public DashboardKpiDTO dashboard() {
        return analytiqueService.getDashboard();
    }
}
