package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.EmploymentType;
import com.ascendingcloud.staffing.service.EmploymentTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employment-types")
@CrossOrigin(origins = "*")
public class EmploymentTypeController {

    private final EmploymentTypeService employmentTypeService;

    public EmploymentTypeController(
            EmploymentTypeService employmentTypeService) {
        this.employmentTypeService = employmentTypeService;
    }

    @GetMapping
    public ResponseEntity<List<EmploymentType>> getAllEmploymentTypes() {
        return ResponseEntity.ok(
                employmentTypeService.getAllEmploymentTypes()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmploymentType> getEmploymentTypeById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(
                employmentTypeService.getEmploymentTypeById(id)
        );
    }

    @PostMapping
    public ResponseEntity<EmploymentType> createEmploymentType(
            @RequestBody EmploymentType employmentType) {
        return ResponseEntity.ok(
                employmentTypeService.createEmploymentType(employmentType)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmploymentType> updateEmploymentType(
            @PathVariable Integer id,
            @RequestBody EmploymentType employmentType) {
        return ResponseEntity.ok(
                employmentTypeService.updateEmploymentType(
                        id, employmentType
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploymentType(
            @PathVariable Integer id) {
        employmentTypeService.deleteEmploymentType(id);
        return ResponseEntity.noContent().build();
    }
}