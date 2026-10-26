package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.CloudPlatform;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.CloudPlatformRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CloudPlatformService {

    private final CloudPlatformRepository repository;

    public CloudPlatformService(CloudPlatformRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CloudPlatform> getAllCloudPlatforms() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public CloudPlatform getCloudPlatformById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cloud platform not found with id: " + id));
    }

    @Transactional
    public CloudPlatform createCloudPlatform(CloudPlatform entity) {
        String name = cleanName(entity.getName());

        CloudPlatform created = new CloudPlatform();
        created.setName(name);

        return repository.save(created);
    }

    @Transactional
    public CloudPlatform updateCloudPlatform(Integer id, CloudPlatform entity) {
        CloudPlatform existing = getCloudPlatformById(id);

        existing.setName(cleanName(entity.getName()));

        return repository.save(existing);
    }

    @Transactional
    public void deleteCloudPlatform(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cloud platform not found with id: " + id);
        }

        repository.deleteById(id);
    }

    private String cleanName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Cloud platform name is required");
        }

        return name.trim();
    }
}
