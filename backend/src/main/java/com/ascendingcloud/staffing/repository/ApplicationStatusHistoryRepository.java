package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.ApplicationStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationStatusHistoryRepository
        extends JpaRepository<ApplicationStatusHistory, Integer> {

    List<ApplicationStatusHistory> findByApplicationIdOrderByChangedAtAsc(
            Integer applicationId
    );
}
    
