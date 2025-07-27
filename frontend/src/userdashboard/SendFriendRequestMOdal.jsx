import React, { useState,useEffect } from 'react';
import axios from '../api/axiosInstance'; 

function SendFriendRequestModal({ onClose }) {
  const [search, setSearch] = useState('');
  const [results, setResults] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [message, setMessage] = useState('');
  const [status, setStatus] = useState('');

 useEffect(() => {
    const fetchResults = async () => {
      if (!search.trim()) {
        setResults([]);
        return;
      }
      try {
        const token = localStorage.getItem('token');
        const res = await axios.get(
          `http://localhost:8080/api/users/searchforFriendRequest?query=${search}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setResults(res.data);
      } catch (err) {
        setStatus('Error searching users');
      }
    };
    fetchResults();
  }, [search]);

  const handleSendRequest = async () => {
    if (!selectedUser) return;
    try {
      const token = localStorage.getItem('token');
      await axios.post(
        `http://localhost:8080/api/friend-requests`,
        { receiver: selectedUser.username, msg:message },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      window.alert('Friend request sent!');
      onClose(); 
    } catch (err) {
      setStatus('Failed to send request');
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h3>Send Friend Request</h3>
        <input
          type="text"
          placeholder="Search username"
          value={search}
          onChange={e => setSearch(e.target.value)}
        />
        <ul>
          {results.map(user => (
            <li key={user.username}>
              {user.username}
              <button onClick={() => setSelectedUser(user)}>Select</button>
            </li>
          ))}
        </ul>
        {selectedUser && (
          <>
            <div>
              <b>To:</b> {selectedUser.username}
            </div>
            <textarea
              placeholder="Optional message"
              value={message}
              onChange={e => setMessage(e.target.value)}
            />
            <button onClick={handleSendRequest}>Send Request</button>
          </>
        )}
        <button onClick={onClose}>Close</button>
        {status && <div>{status}</div>}
      </div>
    </div>
  );
}

export default SendFriendRequestModal;