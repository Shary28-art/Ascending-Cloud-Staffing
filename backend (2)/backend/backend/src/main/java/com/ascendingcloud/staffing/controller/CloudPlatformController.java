package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.CloudPlatform;
import com.ascendingcloud.staffing.repository.CloudPlatformRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cloud-platforms")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174"
})
public class CloudPlatformController {

    private final CloudPlatformRepository repository;

    public CloudPlatformController(
            CloudPlatformRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<CloudPlatform> getAll() {
        return repository.findAll();
    }
}