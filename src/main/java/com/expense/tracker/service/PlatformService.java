package com.expense.tracker.service;

import com.expense.tracker.entity.Platform;
import java.util.List;

public interface PlatformService {
    Platform addPlatform(Platform platform, Long userId);
    Platform updatePlatform(Long id, Platform platform);
    void deletePlatform(Long id);
    List<Platform> getPlatformsByUserId(Long userId);
}
