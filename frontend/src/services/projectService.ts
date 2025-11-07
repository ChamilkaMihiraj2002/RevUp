import axios from 'axios';

const API_BASE = 'http://localhost:8080/api/v1';

export interface ProjectResponse {
  projectId: number;
  userId: number;
  vehicleId?: number;
  technicianId?: number;
  description: string;
  status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED' | 'ON_HOLD';
  estimateTime?: number;
  estimatedAmount?: number;
  startTime?: string;
  endTime?: string;
  createdAt: string;
  updatedAt: string;
}

export interface AcceptProjectRequest {
  technicianId: number;
  estimatedAmount: number;
  estimateTime: number;
}

export interface CreateProjectRequest {
  userId: number;
  vehicleId?: number;
  description: string;
}

export const createProject = async (request: CreateProjectRequest, token: string): Promise<ProjectResponse> => {
  const response = await axios.post(`${API_BASE}/projects`, request, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });
  return response.data;
};

export const getProjectsByUserId = async (userId: number, token: string): Promise<ProjectResponse[]> => {
  const response = await axios.get(`${API_BASE}/projects/user/${userId}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};

export const getPendingProjects = async (token: string): Promise<ProjectResponse[]> => {
  const response = await axios.get(`${API_BASE}/projects/pending`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};

export const getProjectsByTechnicianId = async (technicianId: number, token: string): Promise<ProjectResponse[]> => {
  const response = await axios.get(`${API_BASE}/projects/technician/${technicianId}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};

export const acceptProject = async (
  projectId: number, 
  request: AcceptProjectRequest, 
  token: string
): Promise<ProjectResponse> => {
  const response = await axios.post(`${API_BASE}/projects/${projectId}/accept`, request, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};
