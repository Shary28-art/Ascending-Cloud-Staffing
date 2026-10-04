package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.ApplicationStatusHistory;
import com.ascendingcloud.staffing.repository.ApplicationStatusHistoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/application-status-history")
@CrossOrigin(origins = "*")
public class ApplicationStatusHistoryController {

    private final ApplicationStatusHistoryRepository repository;

    public ApplicationStatusHistoryController(
            ApplicationStatusHistoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<ApplicationStatusHistory>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationStatusHistory> getById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application status history not found"))
        );
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<ApplicationStatusHistory>> getByApplication(
            @PathVariable Integer applicationId) {

        return ResponseEntity.ok(
                repository.findByApplicationIdOrderByChangedAtAsc(
                        applicationId
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApplicationStatusHistory> create(
            @RequestBody ApplicationStatusHistory history) {

        return ResponseEntity.ok(repository.save(history));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationStatusHistory> update(
            @PathVariable Integer id,
            @RequestBody ApplicationStatusHistory history) {

        ApplicationStatusHistory existing =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application status history not found"));

        existing.setApplication(history.getApplication());
        existing.setStatus(history.getStatus());
        existing.setChangedAt(history.getChangedAt());

        return ResponseEntity.ok(repository.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException(
                    "Application status history not found");
        }

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}