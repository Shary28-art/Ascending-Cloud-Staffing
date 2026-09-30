package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.ApplicationStatusHistory;
import com.ascendingcloud.staffing.repository.ApplicationStatusHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationStatusHistoryService {

    private final ApplicationStatusHistoryRepository repository;

    public ApplicationStatusHistoryService(
            ApplicationStatusHistoryRepository repository) {
        this.repository = repository;
    }

    public List<ApplicationStatusHistory> getAllHistory() {
        return repository.findAll();
    }

    public ApplicationStatusHistory getHistoryById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Application status history not found"));
    }

    public List<ApplicationStatusHistory> getHistoryByApplicationId(
            Integer applicationId) {
        return repository.findByApplicationIdOrderByChangedAtAsc(applicationId);
    }

    public ApplicationStatusHistory createHistory(
            ApplicationStatusHistory history) {
        return repository.save(history);
    }

    public ApplicationStatusHistory updateHistory(
            Integer id,
            ApplicationStatusHistory history) {

        ApplicationStatusHistory existingHistory = getHistoryById(id);

        existingHistory.setApplication(history.getApplication());
        existingHistory.setStatus(history.getStatus());
        existingHistory.setChangedAt(history.getChangedAt());

        return repository.save(existingHistory);
    }

    public void deleteHistory(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException(
                    "Application status history not found");
        }

        repository.deleteById(id);
    }
}