import React, { useState, useEffect } from 'react';
import axios from 'axios';
//import './InviteUsersModal.css'; // optional for modal styling

const InviteUsersModal = ({ roomId, onClose }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [userResults, setUserResults] = useState([]);
  const [selectedUsers, setSelectedUsers] = useState([]);
  const [message, setMessage] = useState('');

  const token = localStorage.getItem('token');

  useEffect(() => {
    const fetchUsers = async () => {
      if (searchTerm.trim() === '') {
        setUserResults([]);
        return;
      }
      try {
        const res = await axios.get(`http://localhost:8080/api/users/searchforChatRoomInvite?query=${searchTerm}&chatRoomId=${roomId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setUserResults(res.data);
      } catch (err) {
        console.error('Error fetching users:', err);
      }
    };

    const debounce = setTimeout(fetchUsers, 500);
    return () => clearTimeout(debounce);
  }, [searchTerm]);

  const toggleUser = (user) => {
    const alreadyAdded = selectedUsers.find((u) => u.username === user.username);
    if (alreadyAdded) {
      setSelectedUsers(selectedUsers.filter((u) => u.username !== user.username));
    } else {
      setSelectedUsers([...selectedUsers, user]);
    }
  };

  const handleSendInvites = async () => {
    const requests = selectedUsers.map((user) => ({
      roomId,
      targetUsername: user.username,
      sentByAdmin: true,
      message,
    }));

    try {
      await axios.post(`http://localhost:8080/api/join-requests`, requests, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });
      alert('Invites sent!');
      onClose();
    } catch (err) {
      console.error('Failed to send invites:', err);
      alert('Failed to send invites.');
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h3>Invite Users to Room</h3>
        <input
          type="text"
          placeholder="Search by username..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />

        <ul>
          {userResults.map((user) => (
            <li key={user.username}>
               <label>
                <input
                  type="checkbox"
                  checked={selectedUsers.some((u) => u.username === user.username)}
                  onChange={() => toggleUser(user)}
                />
                {user.username}
              </label>
            </li>
          ))}
        </ul>

        <textarea
          placeholder="Optional message"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
        />

        <div className="modal-buttons">
          <button onClick={handleSendInvites} disabled={selectedUsers.length === 0}>
            Send Invites
          </button>
          <button onClick={onClose}>Cancel</button>
        </div>
      </div>
    </div>
  );
};

export default InviteUsersModal;
