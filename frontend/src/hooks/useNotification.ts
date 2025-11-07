import { useEffect, useRef, useCallback } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { useAuth } from '../contexts/authContext/authContext';

interface UseNotificationsProps {
  onMessage?: (body: any) => void;
}

export default function useNotifications({ onMessage }: UseNotificationsProps) {
  const { currentUser } = useAuth();
  const clientRef = useRef<Client | null>(null);

  useEffect(() => {
    if (!currentUser) {
      console.warn('User not authenticated');
      return;
    }

    // Use Firebase user ID as the username
    const userId = currentUser.uid;
    const url = `http://localhost:8083/websocket?username=${encodeURIComponent(userId)}`;
    
    const client = new Client({
      webSocketFactory: () => new SockJS(url),
      reconnectDelay: 5000,
      onConnect: () => {
        console.log('WebSocket connected successfully for user:', userId);
        // Subscribe to user queue
        client.subscribe('/user/queue/notifications', (msg: any) => {
          let body = msg.body;
          try { body = JSON.parse(body); } catch(e) {}
          console.log('Received notification:', body);
          if (onMessage) onMessage(body);
        });
      },
      onStompError: (frame: any) => {
        console.error('STOMP error', frame);
      },
      onWebSocketError: (error: any) => {
        console.error('WebSocket error', error);
      }
    });
    
    clientRef.current = client;
    client.activate();

    return () => {
      try { 
        client.deactivate(); 
        console.log('WebSocket disconnected');
      } catch(e) {
        console.error('Error during disconnect:', e);
      }
    };
  }, [currentUser, onMessage]);

  const sendNotification = useCallback((to: string, message: string) => {
    const client = clientRef.current;
    if (!client || !client.connected) {
      console.warn('STOMP not connected');
      return;
    }
    client.publish({
      destination: '/app/sendMessage',
      body: JSON.stringify({ to, message }),
      headers: { 'content-type': 'application/json' }
    });
    console.log('Notification sent to:', to);
  }, []);

  return { sendNotification };
}