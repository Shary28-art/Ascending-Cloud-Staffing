package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.EmploymentType;
import com.ascendingcloud.staffing.service.EmploymentTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employment-types")
public class EmploymentTypeController {

    private final EmploymentTypeService employmentTypeService;

    public EmploymentTypeController(
            EmploymentTypeService employmentTypeService) {
        this.employmentTypeService = employmentTypeService;
    }

    @GetMapping
    public ResponseEntity<List<EmploymentType>> getAll() {
        return ResponseEntity.ok(
                employmentTypeService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmploymentType> getById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                employmentTypeService.getById(id)
        );
    }

    @PostMapping
    public ResponseEntity<EmploymentType> create(
            @RequestBody EmploymentType employmentType) {

        return ResponseEntity.ok(
                employmentTypeService.create(employmentType)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmploymentType> update(
            @PathVariable Integer id,
            @RequestBody EmploymentType employmentType) {

        return ResponseEntity.ok(
                employmentTypeService.update(id, employmentType)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id) {

        employmentTypeService.delete(id);

        return ResponseEntity.noContent().build();
    }
}