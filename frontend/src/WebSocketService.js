import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { refreshAccessToken } from './TokenService.js';

let stompClient = null;
  const room=JSON.parse(localStorage.getItem("room"));

export const connect = async (onMessageReceived) => {
    let token = localStorage.getItem('token');
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const now = Date.now() / 1000;
      if (payload.exp - now < 60) {
        token = await refreshAccessToken();
      }
    } catch (err) {
      console.error('Failed to decode JWT, forcing logout');
      localStorage.clear();
      window.location.href = '/login';
      return;
    }
    const socket = new SockJS(`http://localhost:8080/chat?token=${token}`);
  
      stompClient = new Client({

    webSocketFactory: () => socket,

         connectHeaders: {
      Authorization: `Bearer ${token}`  // 🔒 Send JWT here
    },
        reconnectDelay: 5000,
        debug: (str) => console.log(str),
        onConnect: () => {
          console.log('✅ Connected to WebSocket');
        stompClient.subscribe('/topic/room/'+room?.id, (message) => {
           onMessageReceived(JSON.parse(message.body));
         });
       },
          onStompError: async (frame) => {
      console.warn('WebSocket STOMP error:', frame.headers.message);
      if (frame.headers.message.includes("JWT") || frame.headers.message.includes("expired")) {
        try {
          const newToken = await refreshAccessToken();
          localStorage.setItem('token', newToken);
          connect(onMessageReceived); // retry connection
        } catch {
          localStorage.clear();
          window.location.href = '/login';
        }
      }
    },
        });
        stompClient.activate();
 };
  
export const sendMessage = (msg) => {
    const token = localStorage.getItem('token');
    if (stompClient && stompClient.connected)  {
        stompClient.publish({
            destination:'/app/room/'+room?.id,
            body:JSON.stringify(msg),
            headers: {
                Authorization: `Bearer ${token}`  // ✅ include JWT in message headers
              }
        });
    }
};