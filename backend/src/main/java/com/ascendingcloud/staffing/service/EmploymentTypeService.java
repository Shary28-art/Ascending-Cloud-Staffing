package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.EmploymentType;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.EmploymentTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmploymentTypeService {

    private final EmploymentTypeRepository repository;

    public EmploymentTypeService(EmploymentTypeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<EmploymentType> getAllEmploymentTypes() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public EmploymentType getEmploymentTypeById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employment type not found with id: " + id));
    }

    @Transactional
    public EmploymentType createEmploymentType(EmploymentType entity) {
        String name = cleanName(entity.getName());

        EmploymentType created = new EmploymentType();
        created.setName(name);

        return repository.save(created);
    }

    @Transactional
    public EmploymentType updateEmploymentType(Integer id, EmploymentType entity) {
        EmploymentType existing = getEmploymentTypeById(id);

        existing.setName(cleanName(entity.getName()));

        return repository.save(existing);
    }

    @Transactional
    public void deleteEmploymentType(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Employment type not found with id: " + id);
        }

        repository.deleteById(id);
    }

    private String cleanName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Employment type name is required");
        }

        return name.trim();
    }
}
