package com.expense.tracker.controller;

import com.expense.tracker.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "APIs for Dashboard Statistics")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/{userId}")
    @Operation(summary = "Get dashboard statistics for a user")
    public ResponseEntity<Map<String, Object>> getDashboardStats(@PathVariable Long userId) {
        return ResponseEntity.ok(dashboardService.getDashboardStats(userId));
    }
}
