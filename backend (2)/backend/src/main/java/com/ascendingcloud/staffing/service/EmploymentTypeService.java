package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.EmploymentType;
import com.ascendingcloud.staffing.repository.EmploymentTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmploymentTypeService {

    private final EmploymentTypeRepository repository;

    public EmploymentTypeService(EmploymentTypeRepository repository) {
        this.repository = repository;
    }

    public List<EmploymentType> getAll() {
        return repository.findAll();
    }

    public EmploymentType getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employment type not found"));
    }

    public EmploymentType create(EmploymentType employmentType) {
        return repository.save(employmentType);
    }

    public EmploymentType update(
            Integer id,
            EmploymentType employmentType) {

        EmploymentType existing = getById(id);

        existing.setName(employmentType.getName());

        return repository.save(existing);
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Employment type not found");
        }

        repository.deleteById(id);
    }
}