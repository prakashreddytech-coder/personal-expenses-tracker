package com.expense.tracker.service.impl;

import com.expense.tracker.entity.Platform;
import com.expense.tracker.entity.User;
import com.expense.tracker.repository.PlatformRepository;
import com.expense.tracker.repository.UserRepository;
import com.expense.tracker.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlatformServiceImpl implements PlatformService {

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Platform addPlatform(Platform platform, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        platform.setUser(user);
        return platformRepository.save(platform);
    }

    @Override
    public Platform updatePlatform(Long id, Platform platformDetails) {
        Platform platform = platformRepository.findById(id).orElseThrow(() -> new RuntimeException("Platform not found"));
        platform.setName(platformDetails.getName());
        return platformRepository.save(platform);
    }

    @Override
    public void deletePlatform(Long id) {
        platformRepository.deleteById(id);
    }

    @Override
    public List<Platform> getPlatformsByUserId(Long userId) {
        return platformRepository.findByUserId(userId);
    }
}
