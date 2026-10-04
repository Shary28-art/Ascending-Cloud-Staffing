package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.CloudPlatform;
import com.ascendingcloud.staffing.service.CloudPlatformService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cloud-platforms")
public class CloudPlatformController {

    private final CloudPlatformService service;

    public CloudPlatformController(CloudPlatformService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CloudPlatform>> getAllCloudPlatforms() {
        return ResponseEntity.ok(service.getAllCloudPlatforms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CloudPlatform> getCloudPlatformById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getCloudPlatformById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CloudPlatform> createCloudPlatform(@RequestBody CloudPlatform body) {
        return ResponseEntity.status(201).body(service.createCloudPlatform(body));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CloudPlatform> updateCloudPlatform(
            @PathVariable Integer id,
            @RequestBody CloudPlatform body) {
        return ResponseEntity.ok(service.updateCloudPlatform(id, body));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCloudPlatform(@PathVariable Integer id) {
        service.deleteCloudPlatform(id);
        return ResponseEntity.noContent().build();
    }
}
