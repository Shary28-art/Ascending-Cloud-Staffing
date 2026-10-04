package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.EmploymentType;
import com.ascendingcloud.staffing.service.EmploymentTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employment-types")
public class EmploymentTypeController {

    private final EmploymentTypeService service;

    public EmploymentTypeController(EmploymentTypeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EmploymentType>> getAllEmploymentTypes() {
        return ResponseEntity.ok(service.getAllEmploymentTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmploymentType> getEmploymentTypeById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getEmploymentTypeById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmploymentType> createEmploymentType(@RequestBody EmploymentType body) {
        return ResponseEntity.status(201).body(service.createEmploymentType(body));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmploymentType> updateEmploymentType(
            @PathVariable Integer id,
            @RequestBody EmploymentType body) {
        return ResponseEntity.ok(service.updateEmploymentType(id, body));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmploymentType(@PathVariable Integer id) {
        service.deleteEmploymentType(id);
        return ResponseEntity.noContent().build();
    }
}
