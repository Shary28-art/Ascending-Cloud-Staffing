package com.ascendingcloud.staffing.dto;

/** Request body for creating or updating an employer profile. */
public record EmployerProfileDTO(
        String phone,
        String designation,
        Integer companyId,
        String primaryContactName,
        String contactEmail,
        String hiringLocations,
        String employeeTypesNeeded,
        String preferredWorkArrangements,
        String primaryRoles,
        Integer expectedHires,
        String hiringTimeline,
        String billingAddress,
        String selectedPlan,
        String preferredInterviewMethod,
        Boolean placementTermsAccepted,
        String notificationPreferences
) {
}
