/**
 * API Configuration
 * Centralizes API base URL configuration for all services
 */

import axios from 'axios';

// Get API URL from environment variable, fallback to localhost for development
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

// For debugging
if (import.meta.env.DEV) {
  console.log('API Base URL:', API_BASE_URL);
}

// Add request interceptor to log all API calls
if (import.meta.env.DEV) {
  axios.interceptors.request.use(
    (config: any) => {
      console.log('🚀 API Request:', {
        method: config.method?.toUpperCase(),
        url: config.url,
        data: config.data
      });

      return config;
    },
    (error: any) => {
      console.error('❌ API Request Error:', error);
      return Promise.reject(error);
    }
  );

  // Add response interceptor to log responses
  axios.interceptors.response.use(
    (response: any) => {
      console.log('✅ API Response:', {
        status: response.status,
        url: response.config.url,
        data: response.data
      });
      return response;
    },
    (error: any) => {
      console.error('❌ API Response Error:', {
        status: error.response?.status,
        url: error.config?.url,
        message: error.message,
        data: error.response?.data
      });
      return Promise.reject(error);
    }
  );
}

export default API_BASE_URL;
