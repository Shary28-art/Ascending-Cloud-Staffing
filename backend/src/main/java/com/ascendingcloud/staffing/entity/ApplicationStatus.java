package com.ascendingcloud.staffing.entity;

import com.ascendingcloud.staffing.exception.BadRequestException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Application pipeline statuses from the product requirements document.
 * The label is what gets stored in applications.status.
 */
public enum ApplicationStatus {

    // Main pipeline
    APPLIED("Applied"),
    UNDER_REVIEW("Under Review"),
    SHORTLISTED("Shortlisted"),
    SCREENING("Screening"),
    SUBMITTED_TO_EMPLOYER("Submitted to Employer"),
    INTERVIEW_SCHEDULED("Interview Scheduled"),
    INTERVIEW_COMPLETED("Interview Completed"),
    OFFER_EXTENDED("Offer Extended"),
    OFFER_ACCEPTED("Offer Accepted"),
    HIRED_PLACED("Hired/Placed"),

    // Alternative statuses
    ON_HOLD("On Hold"),
    WITHDRAWN("Withdrawn"),
    REJECTED("Rejected"),
    OFFER_DECLINED("Offer Declined"),
    JOB_CLOSED("Job Closed");

    private final String label;

    ApplicationStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /** True when the application can no longer move forward. */
    public boolean isTerminal() {
        return this == HIRED_PLACED
                || this == WITHDRAWN
                || this == REJECTED
                || this == OFFER_DECLINED
                || this == JOB_CLOSED;
    }

    public static Optional<ApplicationStatus> find(String value) {
        if (value == null) {
            return Optional.empty();
        }

        String trimmed = value.trim();

        return Arrays.stream(values())
                .filter(s -> s.label.equalsIgnoreCase(trimmed))
                .findFirst();
    }

    public static ApplicationStatus fromLabel(String value) {
        return find(value).orElseThrow(() ->
                new BadRequestException("Invalid application status: " + value));
    }

    public static List<String> labels() {
        return Arrays.stream(values())
                .map(ApplicationStatus::getLabel)
                .toList();
    }
}
