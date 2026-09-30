package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Integer> {
}