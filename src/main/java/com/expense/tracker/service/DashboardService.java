package com.expense.tracker.service;

import java.util.Map;

public interface DashboardService {
    Map<String, Object> getDashboardStats(Long userId);
}
