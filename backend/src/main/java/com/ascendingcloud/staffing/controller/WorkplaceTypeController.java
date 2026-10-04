package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.WorkplaceType;
import com.ascendingcloud.staffing.service.WorkplaceTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workplace-types")
public class WorkplaceTypeController {

    private final WorkplaceTypeService service;

    public WorkplaceTypeController(WorkplaceTypeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<WorkplaceType>> getAllWorkplaceTypes() {
        return ResponseEntity.ok(service.getAllWorkplaceTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkplaceType> getWorkplaceTypeById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getWorkplaceTypeById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkplaceType> createWorkplaceType(@RequestBody WorkplaceType body) {
        return ResponseEntity.status(201).body(service.createWorkplaceType(body));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkplaceType> updateWorkplaceType(
            @PathVariable Integer id,
            @RequestBody WorkplaceType body) {
        return ResponseEntity.ok(service.updateWorkplaceType(id, body));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteWorkplaceType(@PathVariable Integer id) {
        service.deleteWorkplaceType(id);
        return ResponseEntity.noContent().build();
    }
}
