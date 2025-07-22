import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './css/ChatDashboard.css'; 
import PendingRequestsSection from './PendingRequestsSection.jsx';
import SendFriendRequestModal from './SendFriendRequestModal';



function ChatDashboard() {
  const [chatRooms, setChatRooms] = useState([]);
  const [newRoomName, setNewRoomName] = useState('');
  const [newRoomDescription, setNewRoomDescription] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [joinRequests, setJoinRequests] = useState([]);
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [showFriendModal, setShowFriendModal] = useState(false);
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
    <div style={{ padding: 20 }}>
 
      <h2>Chat Dashboard</h2>

      <button onClick={() => setShowModal(true)}>➕ Create Room</button>

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
              placeholder="Room Description"
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


     { /*Pending join Requests */}
          {/* <JoinRequestList
        joinRequests={joinRequests}
        onRequestClick={(req) => setSelectedRequest(req)}
      />

      {selectedRequest && (
        <JoinRequestModal
          request={selectedRequest}
          onAccept={handleAccept}
          onReject={handleReject}
          onClose={() => setSelectedRequest(null)}
        />
      )} */}

<PendingRequestsSection />
     <button onClick={() => setShowFriendModal(true)}>🤝 Send Friend Request</button>
      {showFriendModal && (
        <SendFriendRequestModal onClose={() => setShowFriendModal(false)} />
      )}



      {/* Room List */}
      <div style={{ marginTop: 20 }}>
        <h3>Your Chat Rooms</h3>
        <ul>
          {chatRooms.map((room) => (
            <li key={room.id}>
              <strong>{room.name}</strong>: {room.description}
              <button onClick={() => handleJoin(room)}>Join</button>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default ChatDashboard;
