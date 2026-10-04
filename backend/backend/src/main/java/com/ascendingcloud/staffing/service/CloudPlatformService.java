package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.CloudPlatform;
import com.ascendingcloud.staffing.repository.CloudPlatformRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CloudPlatformService {

    private final CloudPlatformRepository cloudPlatformRepository;

    public CloudPlatformService(CloudPlatformRepository cloudPlatformRepository) {
        this.cloudPlatformRepository = cloudPlatformRepository;
    }

    public List<CloudPlatform> getAllCloudPlatforms() {
        return cloudPlatformRepository.findAll();
    }

    public CloudPlatform getCloudPlatformById(Integer id) {
        return cloudPlatformRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cloud platform not found"));
    }

    public CloudPlatform createCloudPlatform(CloudPlatform cloudPlatform) {
        return cloudPlatformRepository.save(cloudPlatform);
    }

    public CloudPlatform updateCloudPlatform(
            Integer id,
            CloudPlatform cloudPlatform) {

        CloudPlatform existingCloudPlatform =
                getCloudPlatformById(id);

        existingCloudPlatform.setName(cloudPlatform.getName());

        return cloudPlatformRepository.save(existingCloudPlatform);
    }

    public void deleteCloudPlatform(Integer id) {
        if (!cloudPlatformRepository.existsById(id)) {
            throw new RuntimeException("Cloud platform not found");
        }

        cloudPlatformRepository.deleteById(id);
    }
}