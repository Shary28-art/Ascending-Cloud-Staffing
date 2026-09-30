package com.ascendingcloud.staffing.security;

public class SecurityConstants {

    public static final String SECRET_KEY =
            "AscendingCloudStaffingSecretKeyForJwtAuthentication2026";

    public static final long EXPIRATION_TIME =
            1000L * 60 * 60 * 24;

    private SecurityConstants() {
    }
}