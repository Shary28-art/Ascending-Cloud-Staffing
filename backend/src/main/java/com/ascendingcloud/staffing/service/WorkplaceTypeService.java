package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.WorkplaceType;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.WorkplaceTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkplaceTypeService {

    private final WorkplaceTypeRepository repository;

    public WorkplaceTypeService(WorkplaceTypeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<WorkplaceType> getAllWorkplaceTypes() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public WorkplaceType getWorkplaceTypeById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Workplace type not found with id: " + id));
    }

    @Transactional
    public WorkplaceType createWorkplaceType(WorkplaceType entity) {
        String name = cleanName(entity.getName());

        WorkplaceType created = new WorkplaceType();
        created.setName(name);

        return repository.save(created);
    }

    @Transactional
    public WorkplaceType updateWorkplaceType(Integer id, WorkplaceType entity) {
        WorkplaceType existing = getWorkplaceTypeById(id);

        existing.setName(cleanName(entity.getName()));

        return repository.save(existing);
    }

    @Transactional
    public void deleteWorkplaceType(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Workplace type not found with id: " + id);
        }

        repository.deleteById(id);
    }

    private String cleanName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Workplace type name is required");
        }

        return name.trim();
    }
}
