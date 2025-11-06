import axios from 'axios';

const API_BASE = 'http://localhost:8080/api/v1';

export interface RegisterUserData {
  name: string;
  email: string;
  phone: string;
  address?: string;
  role: 'CUSTOMER' | 'TECHNICIAN';
  firebaseUID: string;
}

export interface UserData {
  userId: number;
  firebaseUID: string;
  email: string;
  name: string;
  phone: string;
  address?: string;
  role: 'CUSTOMER' | 'TECHNICIAN';
}

export const registerUser = async (userData: RegisterUserData) => {
  const response = await axios.post(`${API_BASE}/users`, userData);
  return response.data;
};

export const getUserByFirebaseUID = async (firebaseUID: string, token: string): Promise<UserData> => {
  const response = await axios.get(`${API_BASE}/users/firebase/${firebaseUID}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};
