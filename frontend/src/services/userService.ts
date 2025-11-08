import axios from 'axios';
import API_BASE_URL from '../config/api.config';

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
  const response = await axios.post(`${API_BASE_URL}/users`, userData);
  return response.data;
};

export const getUserByFirebaseUID = async (firebaseUID: string, token: string): Promise<UserData> => {
  const response = await axios.get(`${API_BASE_URL}/users/firebase/${firebaseUID}`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};

export interface UpdateUserData {
  name?: string;
  email?: string;
  phone?: string;
  address?: string;
}

export const updateUser = async (userId: number, userData: UpdateUserData, token: string): Promise<UserData> => {
  const response = await axios.put(`${API_BASE_URL}/users/${userId}`, userData, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.data;
};
