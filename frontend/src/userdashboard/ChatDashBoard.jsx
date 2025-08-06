import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from '../api/axiosInstance'; 
import './css/ChatDashboard.css'; 
import PendingRequestsSection from './PendingRequestsSection.jsx';
import SendFriendRequestModal from './SendFriendRequestModal.jsx';
import PendingFriendRequestsSection from './PendingFriendRequestsSection.jsx';



function ChatDashboard() {
  const [chatRooms, setChatRooms] = useState([]);
  const [newRoomName, setNewRoomName] = useState('');
  const [newRoomDescription, setNewRoomDescription] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [joinRequests, setJoinRequests] = useState([]);
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [showFriendModal, setShowFriendModal] = useState(false);
  const username = localStorage.getItem('username');
  const navigate = useNavigate();


  useEffect(() => {
    const fetchRooms = async () => {
      try {
        const token = localStorage.getItem('token');
        const res = await axios.get('http://localhost:8080/api/rooms', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setChatRooms(res.data);
      } catch (err) {
        console.error('Error fetching rooms:', err);
      }
    };

    fetchRooms();
  }, []);

//   useEffect(() => {
//   const fetchJoinRequests = async () => {
//     try {
//       const token = localStorage.getItem('token');
//       const res = await axios.get('http://localhost:8080/api/join-requests', {
//         headers: {
//           Authorization: `Bearer ${token}`,
//         },
//       });
//       setJoinRequests(res.data); // <== New state for requests
//     } catch (err) {
//       console.error("Failed to fetch join requests", err);
//     }
//   };

//   fetchJoinRequests();
// }, []);


  const createRoom = async () => {
    if (!newRoomName.trim()) return;

    try {
      const token = localStorage.getItem('token');
      const res = await axios.post(
        'http://localhost:8080/api/rooms',
        {
          name: newRoomName,
          description: newRoomDescription,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setChatRooms([...chatRooms, res.data]);
      setNewRoomName('');
      setNewRoomDescription('');
      setShowModal(false);
      alert('Room created successfully!');
    } catch (err) {
      console.error('Error creating room:', err);
    }
  };

  const handleJoin = (room) =>{
    const username=localStorage.getItem('username');
    if(!username){
      alert("username not found login again!")
      navigate('/')
    }
    localStorage.setItem("room", JSON.stringify(room));
    navigate('/chatroom');
  };

  

  return (
  <>
  <div className="dashboard-bg"> </div>
  <div className="logout-container">

  <button className="homepage-btn logout" onClick={() => { localStorage.clear(); navigate('/');   }}>
      Logout
    </button>
  </div>
  <div className="chat-dashboard-container">
 
   <h2 className="chat-dashboard-title">Hello, {username}</h2>

      {/* Modal */}
      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3>Create New Chat Room</h3>
            <input
              type="text"
              placeholder="Room Name"
              value={newRoomName}
              onChange={(e) => setNewRoomName(e.target.value)}
            />
            <textarea
              placeholder="Room Description (Optional)"
              value={newRoomDescription}
              onChange={(e) => setNewRoomDescription(e.target.value)}
            />
            <div className="modal-buttons">
              <button onClick={createRoom}>Create</button>
              <button onClick={() => setShowModal(false)}>Cancel</button>
            </div>
          </div>
        </div>
      )}

      {showFriendModal && (
            <SendFriendRequestModal onClose={() => setShowFriendModal(false)} />
          )}

          {/* Room List */}
        <div style={{ marginTop: 20 }} className='chatroom-header'>
          <h2 className='chatroom-title'>Your Chat Rooms</h2>
          <div className="dropdown-container">
            <button className="dropdown-toggle">⚙️ Actions</button>
            <div className="dropdown-menu">
              <button onClick={() => setShowModal(true)}>➕ Create Room</button>
              <PendingRequestsSection />
            <PendingFriendRequestsSection />
              <button onClick={() => setShowFriendModal(true)}>💌 Send Friend Request</button>
            </div>
          </div>
        </div>
        <ul  className="room-list">
          {chatRooms.map((room) => (
            <li key={room.id}>
              <strong>{room.name}</strong>
              <button onClick={() => handleJoin(room)}>Join</button>
            </li>
          ))}
        </ul>
  </div>
    </>
  );
};

export default ChatDashboard;
