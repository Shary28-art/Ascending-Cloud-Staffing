package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.EmploymentType;
import com.ascendingcloud.staffing.repository.EmploymentTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmploymentTypeService {

    private final EmploymentTypeRepository employmentTypeRepository;

    public EmploymentTypeService(
            EmploymentTypeRepository employmentTypeRepository) {
        this.employmentTypeRepository = employmentTypeRepository;
    }

    public List<EmploymentType> getAllEmploymentTypes() {
        return employmentTypeRepository.findAll();
    }

    public EmploymentType getEmploymentTypeById(Integer id) {
        return employmentTypeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employment type not found"));
    }

    public EmploymentType createEmploymentType(
            EmploymentType employmentType) {
        return employmentTypeRepository.save(employmentType);
    }

    public EmploymentType updateEmploymentType(
            Integer id,
            EmploymentType employmentType) {

        EmploymentType existingEmploymentType =
                getEmploymentTypeById(id);

        existingEmploymentType.setName(employmentType.getName());

        return employmentTypeRepository.save(existingEmploymentType);
    }

    public void deleteEmploymentType(Integer id) {
        if (!employmentTypeRepository.existsById(id)) {
            throw new RuntimeException("Employment type not found");
        }

        employmentTypeRepository.deleteById(id);
    }
}