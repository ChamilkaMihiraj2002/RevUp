import axios from 'axios';
import API_BASE_URL from '../config/api.config';

export interface VehicleResponse {
  vehicleId: number;
  model: string;
  registrationNo: string;
  year: number;
  color: string;
  vehicleType: string;
  userId: number;
}

export const getVehiclesByUserId = async (userId: number, token: string): Promise<VehicleResponse[]> => {
  const response = await axios.get(`${API_BASE_URL}/vehicles/user/${userId}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};

export const getVehicleById = async (vehicleId: number, token: string): Promise<VehicleResponse> => {
  const response = await axios.get(`${API_BASE_URL}/vehicles/${vehicleId}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};

export const createVehicle = async (vehicleData: Omit<VehicleResponse, 'vehicleId'>, token: string): Promise<VehicleResponse> => {
  const response = await axios.post(`${API_BASE_URL}/vehicles`, vehicleData, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });
  return response.data;
};

export const updateVehicle = async (vehicleId: number, vehicleData: Omit<VehicleResponse, 'vehicleId'>, token: string): Promise<VehicleResponse> => {
  const response = await axios.put(`${API_BASE_URL}/vehicles/${vehicleId}`, vehicleData, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });
  return response.data;
};

export const deleteVehicle = async (vehicleId: number, token: string): Promise<void> => {
  await axios.delete(`${API_BASE_URL}/vehicles/${vehicleId}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
};
