package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.JobAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobAlertRepository extends JpaRepository<JobAlert, Integer> {

    List<JobAlert> findByUserId(Integer userId);

}