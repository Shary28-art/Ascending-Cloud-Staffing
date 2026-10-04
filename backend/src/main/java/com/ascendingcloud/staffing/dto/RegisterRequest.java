package com.ascendingcloud.staffing.dto;

/**
 * Account creation request (see requirements document).
 *
 * Common (all roles): firstName, lastName, email, phone, password,
 *   confirmPassword, role (CANDIDATE or EMPLOYER), termsAccepted.
 *
 * CANDIDATE also needs: city, state, country, preferredJobTitle,
 *   workAuthorizationStatus, recruitmentConsent. The resume is uploaded
 *   afterwards with POST /api/resumes.
 *
 * EMPLOYER also needs: companyName, jobTitle. companyWebsite is optional.
 *
 * "name" is a legacy single-field alternative to firstName + lastName.
 */
public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String phone,
        String password,
        String confirmPassword,
        String role,
        Boolean termsAccepted,
        Boolean recruitmentConsent,
        String city,
        String state,
        String country,
        String preferredJobTitle,
        String workAuthorizationStatus,
        String companyName,
        String jobTitle,
        String companyWebsite,
        String name
) {
}
