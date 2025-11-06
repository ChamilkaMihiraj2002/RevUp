package com.revup.api_gateway.filter;

import com.revup.api_gateway.dto.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

/**
 * HTTP Request Wrapper that adds user context headers
 * These headers will be forwarded to downstream services
 */
public class UserContextRequest extends HttpServletRequestWrapper {

    private final Long userId;
    private final Role role;
    private final String firebaseUID;

    public UserContextRequest(HttpServletRequest request, Long userId, Role role, String firebaseUID) {
        super(request);
        this.userId = userId;
        this.role = role;
        this.firebaseUID = firebaseUID;
    }

    @Override
    public String getHeader(String name) {
        // Add custom headers for downstream services
        if ("X-User-Id".equalsIgnoreCase(name)) {
            return userId != null ? userId.toString() : null;
        }
        if ("X-User-Role".equalsIgnoreCase(name)) {
            return role != null ? role.name() : null;
        }
        if ("X-Firebase-UID".equalsIgnoreCase(name)) {
            return firebaseUID;
        }
        return super.getHeader(name);
    }
}
