import { useState ,useEffect} from 'react';
import { useNavigate } from "react-router-dom";
import { connect, sendMessage } from '../WebSocketService';
import InviteUsersModal from './InviteUsersModal';
import './css/ChatRoom.css';

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
  const navigate = useNavigate();



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
    try{
      if (msgInput.trim()) {
      setSender(user);
      sendMessage({ sender:user, content: msgInput,room:room?.id });
      setMsgInput('');
      }
    }catch (error) {
      connect(handleReceive); // Reconnect if there's an error
    }
  
  };

    if (!isAuthenticated) {
    return <Login onLogin={() => setIsAuthenticated(true)} />;
  }
  
  const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleDateString(undefined, {
      weekday: 'short',
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  const formatTime = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleTimeString(undefined, {
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  const groupedMessages = messages.reduce((acc, msg) => {
    const dateKey = formatDate(msg.timestamp);
    if (!acc[dateKey]) acc[dateKey] = [];
    acc[dateKey].push(msg);
    return acc;
  }, {});
            


return (
  <>
  <div className="chatroom-wrapper"></div>
<div className="top-right-btn-container">
  <button className="homepage-btn login" onClick={() => navigate('/dashboard')}>
    Dashboard
  </button>
  <button
    className="homepage-btn logout"
    onClick={() => {
      localStorage.clear();
      navigate('/');
    }}
  >
    Logout
  </button>
</div>


    <div className="chatroom-container">
      <div className="chatroom-header">
        <h2>Room: {room?.name}</h2>
        <h2>Welcome, {user}</h2>
      </div>

      {room?.admin && (
        <button className="chatroom-btn" onClick={() => setShowInviteModal(true)}>
          ➕ Add Users
        </button>
      )}

      {showInviteModal && (
        <InviteUsersModal roomId={room?.id} onClose={() => setShowInviteModal(false)} />
      )}

       <div className="message-box">
        {Object.entries(groupedMessages).map(([date, msgs]) => (
          <div key={date}>
            <div className="date-header">{date}</div>
            {msgs.map((m, i) => (
              <div className="message" key={i}>
                <span className="timestamp">[{formatTime(m.timestamp)}]</span>
                <b>{m.sender}:</b> {m.content}
              </div>
            ))}
          </div>
        ))}
      </div>


      <div className="input-section">
        <input
          className="chat-input"
          value={msgInput}
          onChange={(e) => setMsgInput(e.target.value)}
          onKeyDown={(e) => e.key === 'Enter' && send()}
          placeholder="Type your message..."
        />
        <button className="chatroom-btn" onClick={send} disabled={!msgInput.trim()}>
          Send
        </button>
      </div>
    </div>
    </>
);

}

export default ChatRoom;