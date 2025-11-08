import axios from 'axios';
import { API_BASE_URL } from '@/config/api.config';

const API_URL = `${API_BASE_URL}/chat`;

export interface ChatResponse {
  results: Array<{
    output: {
      content: string;
    };
  }>;
}

/**
 * Send a message to the chatbot service
 * @param message - The user's message/question
 * @returns The chatbot's response
 */
export const sendChatMessage = async (message: string): Promise<string> => {
  try {
    const response = await axios.get<ChatResponse>(API_URL, {
      params: {
        q: message
      }
    });

    // Extract the response text from the API response
    if (response.data?.results?.[0]?.output?.content) {
      return response.data.results[0].output.content;
    }

    return "I'm sorry, I couldn't process your request. Please try again.";
  } catch (error) {
    console.error('Error sending chat message:', error);
    
    if (axios.isAxiosError(error)) {
      if (error.response?.status === 401) {
        return "Please log in to use the chatbot service.";
      } else if (error.response?.status === 503) {
        return "The chatbot service is currently unavailable. Please try again later.";
      }
    }
    
    return "I'm sorry, something went wrong. Please try again later.";
  }
};
