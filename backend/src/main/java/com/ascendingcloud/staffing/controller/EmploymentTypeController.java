package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.EmploymentType;
import com.ascendingcloud.staffing.repository.EmploymentTypeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employment-types")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174"
})
public class EmploymentTypeController {

    private final EmploymentTypeRepository repository;

    public EmploymentTypeController(
            EmploymentTypeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<EmploymentType> getAll() {
        return repository.findAll();
    }
}