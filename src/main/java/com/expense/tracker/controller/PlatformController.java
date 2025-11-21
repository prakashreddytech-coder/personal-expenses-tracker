package com.expense.tracker.controller;

import com.expense.tracker.entity.Platform;
import com.expense.tracker.service.PlatformService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/platforms")
@Tag(name = "Platform Management", description = "APIs for managing Payment Platforms")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @PostMapping
    @Operation(summary = "Add a new platform")
    public ResponseEntity<Platform> addPlatform(@Valid @RequestBody Platform platform, @RequestParam Long userId) {
        return ResponseEntity.ok(platformService.addPlatform(platform, userId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update platform details")
    public ResponseEntity<Platform> updatePlatform(@PathVariable Long id, @RequestBody Platform platform) {
        return ResponseEntity.ok(platformService.updatePlatform(id, platform));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a platform")
    public ResponseEntity<Void> deletePlatform(@PathVariable Long id) {
        platformService.deletePlatform(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all platforms for a user")
    public ResponseEntity<List<Platform>> getPlatformsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(platformService.getPlatformsByUserId(userId));
    }
}
