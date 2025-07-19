import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

let stompClient = null;
  const room=JSON.parse(localStorage.getItem("room"));

export const connect = (onMessageReceived) => {
    const token = localStorage.getItem('token');
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
        onStompError: (frame) => {
            console.error('Broker error:', frame);
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