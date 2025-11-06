import axios from 'axios';

const API_BASE = 'http://localhost:8080/api/v1';

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
  const response = await axios.get(`${API_BASE}/vehicles/user/${userId}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};

export const getVehicleById = async (vehicleId: number, token: string): Promise<VehicleResponse> => {
  const response = await axios.get(`${API_BASE}/vehicles/${vehicleId}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};

export const createVehicle = async (vehicleData: Omit<VehicleResponse, 'vehicleId'>, token: string): Promise<VehicleResponse> => {
  const response = await axios.post(`${API_BASE}/vehicles`, vehicleData, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });
  return response.data;
};
