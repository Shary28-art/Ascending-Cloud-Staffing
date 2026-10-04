package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Integer> {

    /**
     * Public job board query: only admin-approved, active jobs whose
     * application deadline has not passed. Pass "" (not null) for no filter.
     */
    @Query("""
            select j from Job j
            where j.approvalStatus = :approval
              and j.status = :status
              and (j.applicationDeadline is null or j.applicationDeadline >= :today)
              and (:title = '' or lower(j.title) like lower(concat('%', :title, '%')))
              and (:location = '' or lower(j.location) like lower(concat('%', :location, '%')))
            order by j.createdAt desc
            """)
    List<Job> searchPublic(
            @Param("approval") String approval,
            @Param("status") String status,
            @Param("today") LocalDate today,
            @Param("title") String title,
            @Param("location") String location
    );

    List<Job> findAllByOrderByCreatedAtDesc();

    List<Job> findByCompanyIdOrderByCreatedAtDesc(Integer companyId);

    List<Job> findByApprovalStatusOrderByCreatedAtDesc(String approvalStatus);
}
