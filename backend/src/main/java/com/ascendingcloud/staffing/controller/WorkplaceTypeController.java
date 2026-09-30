package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.WorkplaceType;
import com.ascendingcloud.staffing.repository.WorkplaceTypeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workplace-types")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174"
})
public class WorkplaceTypeController {

    private final WorkplaceTypeRepository repository;

    public WorkplaceTypeController(
            WorkplaceTypeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<WorkplaceType> getAll() {
        return repository.findAll();
    }
}