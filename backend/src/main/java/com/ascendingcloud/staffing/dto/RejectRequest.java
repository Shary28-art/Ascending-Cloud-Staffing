package com.ascendingcloud.staffing.dto;

/** Body for admin job rejection, e.g. {"reason": "Missing salary range"}. */
public record RejectRequest(String reason) {
}
