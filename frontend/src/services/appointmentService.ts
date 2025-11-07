import axios from 'axios';

const API_BASE = 'http://localhost:8080/api/v1';

export interface AppointmentResponse {
  appointmentId: number;
  customerId: number;
  vehicleId: number;
  technicianId?: number;
  status: 'SCHEDULED' | 'CONFIRMED' | 'ONGOING' | 'SERVICED' | 'COMPLETED' | 'CANCELLED';
  scheduledStart: string;
  scheduledEnd: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateAppointmentRequest {
  customerId: number;
  vehicleId: number;
  scheduledStart: string; // ISO 8601 format
  scheduledEnd: string;   // ISO 8601 format
}

export interface SlotAvailabilityResponse {
  available: boolean;
  bookedCount: number;
  maxCapacity: number;
  message: string;
}

export const getAppointmentsByCustomerId = async (customerId: number, token: string): Promise<AppointmentResponse[]> => {
  const response = await axios.get(`${API_BASE}/appointments/customer/${customerId}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};

export const getAppointmentsByVehicleId = async (vehicleId: number, token: string): Promise<AppointmentResponse[]> => {
  const response = await axios.get(`${API_BASE}/appointments/vehicle/${vehicleId}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};

export const getAppointmentById = async (appointmentId: number, token: string): Promise<AppointmentResponse> => {
  const response = await axios.get(`${API_BASE}/appointments/${appointmentId}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};

export const createAppointment = async (request: CreateAppointmentRequest, token: string): Promise<AppointmentResponse> => {
  const response = await axios.post(`${API_BASE}/appointments`, request, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });
  return response.data;
};

export const checkSlotAvailability = async (date: string, time: string, token: string): Promise<SlotAvailabilityResponse> => {
  const response = await axios.get(`${API_BASE}/appointments/slots/availability`, {
    params: {
      date,
      time
    },
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};

export interface UpdateAppointmentRequest {
  status?: 'SCHEDULED' | 'CONFIRMED' | 'ONGOING' | 'SERVICED' | 'COMPLETED' | 'CANCELLED';
  technicianId?: number;
}

export const updateAppointment = async (appointmentId: number, request: UpdateAppointmentRequest, token: string): Promise<AppointmentResponse> => {
  const response = await axios.put(`${API_BASE}/appointments/${appointmentId}`, request, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });
  return response.data;
};

export const getAllAppointments = async (token: string): Promise<AppointmentResponse[]> => {
  const response = await axios.get(`${API_BASE}/appointments`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};

export const getUnassignedAppointments = async (token: string): Promise<AppointmentResponse[]> => {
  const response = await axios.get(`${API_BASE}/appointments/unassigned`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};

export const getAppointmentsByTechnicianId = async (technicianId: number, token: string): Promise<AppointmentResponse[]> => {
  const response = await axios.get(`${API_BASE}/appointments/technician/${technicianId}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};
