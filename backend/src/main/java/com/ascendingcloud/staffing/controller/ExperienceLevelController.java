package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.ExperienceLevel;
import com.ascendingcloud.staffing.repository.ExperienceLevelRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/experience-levels")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174"
})
public class ExperienceLevelController {

    private final ExperienceLevelRepository repository;

    public ExperienceLevelController(
            ExperienceLevelRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<ExperienceLevel> getAll() {
        return repository.findAll();
    }
}