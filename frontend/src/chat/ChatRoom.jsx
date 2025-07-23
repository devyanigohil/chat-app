import { useState ,useEffect, useContext} from 'react'
import { connect, sendMessage } from '../WebSocketService';
import InviteUsersModal from './InviteUsersModal';

function ChatRoom(){
  const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('token'));
  const [messages, setMessages] = useState([]);
  const [sender, setSender] = useState('');
  const [msgInput, setMsgInput] = useState('');
 // const [connected, setConnected] = useState(false);
  const [user,setUser] = useState(localStorage.getItem("username") || '');
  const [room,setRoom] = useState(() => {
                              const storedRoom = localStorage.getItem("room");
                              return storedRoom ? JSON.parse(storedRoom) : null;
                            });
  const [showInviteModal, setShowInviteModal] = useState(false);



  const handleReceive = (msg) => {
    setMessages((prev) => [...prev, msg]);
  };

    useEffect(() => {
    if (room?.id) {
      fetch(`http://localhost:8080/api/rooms/${room.id}/messages`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      })
        .then(res => res.json())
        .then(data => setMessages(data))
        .catch(err => console.error('Failed to fetch messages', err));
    }
  }, []);

  useEffect(() => {
    if (room && user) {
      connect(handleReceive);
      // setConnected(true);
    }
  }, [room, user]);

  const send = () => {
    if (msgInput.trim()) {
      setSender(user);
      sendMessage({ sender:user, content: msgInput,room:room?.id });
      setMsgInput('');
    }
  };

    if (!isAuthenticated) {
    return <Login onLogin={() => setIsAuthenticated(true)} />;
  }
  
          
    return(

         <div style={{ padding: 20 }}>
          
        <>
        <h2>Room: {room?.name}</h2>
          <h2>Welcome, {user}!</h2>
                  
          {room?.admin &&(        
          <button onClick={() => setShowInviteModal(true)}>➕ Add Users</button>
          )}
            {showInviteModal && (
                          <InviteUsersModal roomId={room?.id} onClose={() => setShowInviteModal(false)} />
            )}

            
          <div style={{ height: 300, overflow: 'auto', border: '1px solid gray', marginBottom: 10 }}>
            {messages.map((m, i) => (
              <div key={i}>
                <b>{m.sender}:</b> {m.content}
              </div>
            ))}
          </div>
          <input
            value={msgInput}
            onChange={(e) => setMsgInput(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && send()}
            placeholder="Type your message..."
          />
          <button onClick={send}>Send</button>
        </>
      
    </div>
    );
}

export default ChatRoom;