package com.revup.notification_service.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class WebSocketSessionManager {

    private static final String SESSION_KEY_PREFIX = "websocket:session:";
    private static final String USER_SESSIONS_PREFIX = "websocket:user:";
    private static final long SESSION_TTL_HOURS = 24;

    private final RedisTemplate<String, Object> redisTemplate;

    public WebSocketSessionManager(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Register a WebSocket session for a user
     */
    public void registerSession(String sessionId, Long userId) {
        if (sessionId == null || userId == null) return;
        
        String sessionKey = SESSION_KEY_PREFIX + sessionId;
        String userSessionsKey = USER_SESSIONS_PREFIX + userId;
        
        // Store session info
        redisTemplate.opsForValue().set(sessionKey, userId, SESSION_TTL_HOURS, TimeUnit.HOURS);
        
        // Add session to user's sessions set
        redisTemplate.opsForSet().add(userSessionsKey, sessionId);
        redisTemplate.expire(userSessionsKey, SESSION_TTL_HOURS, TimeUnit.HOURS);
    }

    /**
     * Unregister a WebSocket session
     */
    public void unregisterSession(String sessionId) {
        if (sessionId == null) return;
        
        String sessionKey = SESSION_KEY_PREFIX + sessionId;
        Object userId = redisTemplate.opsForValue().get(sessionKey);
        
        if (userId != null) {
            String userSessionsKey = USER_SESSIONS_PREFIX + userId;
            redisTemplate.opsForSet().remove(userSessionsKey, sessionId);
        }
        
        redisTemplate.delete(sessionKey);
    }

    /**
     * Get all active session IDs for a user
     */
    public Set<Object> getUserSessions(Long userId) {
        if (userId == null) return Set.of();
        String userSessionsKey = USER_SESSIONS_PREFIX + userId;
        return redisTemplate.opsForSet().members(userSessionsKey);
    }

    /**
     * Get user ID for a session
     */
    public Long getUserIdForSession(String sessionId) {
        if (sessionId == null) return null;
        String sessionKey = SESSION_KEY_PREFIX + sessionId;
        Object userId = redisTemplate.opsForValue().get(sessionKey);
        
        if (userId instanceof Number) {
            return ((Number) userId).longValue();
        } else if (userId instanceof String) {
            try {
                return Long.parseLong((String) userId);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Check if a session is active
     */
    public boolean isSessionActive(String sessionId) {
        if (sessionId == null) return false;
        String sessionKey = SESSION_KEY_PREFIX + sessionId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(sessionKey));
    }

    /**
     * Refresh session TTL
     */
    public void refreshSession(String sessionId) {
        if (sessionId == null) return;
        String sessionKey = SESSION_KEY_PREFIX + sessionId;
        redisTemplate.expire(sessionKey, SESSION_TTL_HOURS, TimeUnit.HOURS);
    }
}
