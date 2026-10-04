package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.Skill;
import com.ascendingcloud.staffing.service.SkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
public class SkillController {

    private final SkillService service;

    public SkillController(SkillService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Skill>> getAllSkills() {
        return ResponseEntity.ok(service.getAllSkills());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Skill> getSkillById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getSkillById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Skill> createSkill(@RequestBody Skill body) {
        return ResponseEntity.status(201).body(service.createSkill(body));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Skill> updateSkill(
            @PathVariable Integer id,
            @RequestBody Skill body) {
        return ResponseEntity.ok(service.updateSkill(id, body));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSkill(@PathVariable Integer id) {
        service.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}
