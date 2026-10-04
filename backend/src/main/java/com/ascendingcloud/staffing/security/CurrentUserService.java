package com.ascendingcloud.staffing.security;

import com.ascendingcloud.staffing.entity.EmployerProfile;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.repository.EmployerProfileRepository;
import com.ascendingcloud.staffing.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Resolves the logged-in user from the JWT instead of trusting an
 * "email" request parameter sent by the client.
 */
@Service
public class CurrentUserService {

    public static final String CANDIDATE = "CANDIDATE";
    public static final String EMPLOYER = "EMPLOYER";
    public static final String ADMIN = "ADMIN";

    private final UserRepository userRepository;
    private final EmployerProfileRepository employerProfileRepository;

    public CurrentUserService(
            UserRepository userRepository,
            EmployerProfileRepository employerProfileRepository) {
        this.userRepository = userRepository;
        this.employerProfileRepository = employerProfileRepository;
    }

    /** The logged-in user, or null for anonymous requests. */
    public User getUserOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null
                || !auth.isAuthenticated()
                || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }

        return userRepository.findByEmail(auth.getName()).orElse(null);
    }

    public User getUser() {
        User user = getUserOrNull();

        if (user == null) {
            throw new BadCredentialsException("Authentication required");
        }

        return user;
    }

    public boolean isAdmin(User user) {
        return user != null && ADMIN.equalsIgnoreCase(user.getRole());
    }

    public boolean isEmployer(User user) {
        return user != null && EMPLOYER.equalsIgnoreCase(user.getRole());
    }

    public boolean isCandidate(User user) {
        return user != null && CANDIDATE.equalsIgnoreCase(user.getRole());
    }

    /** Company id of the logged-in employer, or null if they have none yet. */
    public Integer getEmployerCompanyId(User user) {
        return employerProfileRepository.findByUserId(user.getId())
                .map(EmployerProfile::getCompany)
                .map(c -> c.getId())
                .orElse(null);
    }

    /** Throws 403 unless the user is an admin or an employer of that company. */
    public void requireCompanyAccess(User user, Integer companyId) {
        if (isAdmin(user)) {
            return;
        }

        if (isEmployer(user)
                && companyId != null
                && companyId.equals(getEmployerCompanyId(user))) {
            return;
        }

        throw new AccessDeniedException(
                "You do not have access to this company's data");
    }
}
