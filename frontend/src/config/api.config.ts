/**
 * API Configuration
 * Centralizes API base URL configuration for all services
 */

// Get API URL from environment variable, fallback to localhost for development
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

// For debugging
if (import.meta.env.DEV) {
  console.log('API Base URL:', API_BASE_URL);
}

export default API_BASE_URL;
