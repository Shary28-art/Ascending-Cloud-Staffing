package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
}