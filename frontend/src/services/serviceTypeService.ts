import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/v1';

export interface ServiceTypeResponse {
  serviceTypeId: number;
  code: string;
  name: string;
  description: string;
  baseDurationMinutes: number;
  basePrice: number;
}

export const getAllServiceTypes = async (token: string): Promise<ServiceTypeResponse[]> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/service-types`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching service types:', error);
    throw error;
  }
};
