package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.WorkplaceType;
import com.ascendingcloud.staffing.repository.WorkplaceTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkplaceTypeService {

    private final WorkplaceTypeRepository workplaceTypeRepository;

    public WorkplaceTypeService(
            WorkplaceTypeRepository workplaceTypeRepository) {
        this.workplaceTypeRepository = workplaceTypeRepository;
    }

    public List<WorkplaceType> getAllWorkplaceTypes() {
        return workplaceTypeRepository.findAll();
    }

    public Optional<WorkplaceType> getWorkplaceTypeById(Integer id) {
        return workplaceTypeRepository.findById(id);
    }

    public WorkplaceType createWorkplaceType(
            WorkplaceType workplaceType) {
        return workplaceTypeRepository.save(workplaceType);
    }

    public WorkplaceType updateWorkplaceType(
            Integer id,
            WorkplaceType details) {

        Optional<WorkplaceType> optional =
                workplaceTypeRepository.findById(id);

        if (optional.isEmpty()) {
            return null;
        }

        WorkplaceType workplaceType = optional.get();
        workplaceType.setName(details.getName());

        return workplaceTypeRepository.save(workplaceType);
    }

    public boolean deleteWorkplaceType(Integer id) {
        if (!workplaceTypeRepository.existsById(id)) {
            return false;
        }

        workplaceTypeRepository.deleteById(id);
        return true;
    }
}