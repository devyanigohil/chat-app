import React, { useEffect, useState } from 'react';
import axios from 'axios';
import FriendRequestList from './FriendRequestList'; // You created this earlier
import FriendRequestModal from './FriendRequestModal'; // Modal for accept/reject

const PendingFriendRequestsSection = () => {
  const [requests, setRequests] = useState([]);
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [showModal, setShowModal] = useState(false);

  const showPendingRequests = () => setShowModal(true);
  const closeModal = () => setShowModal(false);

  useEffect(() => {
    const fetchFriendRequests = async () => {
      try {
        const token = localStorage.getItem('token');
        const res = await axios.get('http://localhost:8080/api/friend-requests', {
          headers: { Authorization: `Bearer ${token}` },
        });
        setRequests(res.data);
      } catch (err) {
        console.error('Error fetching friend requests', err);
      }
    };
    fetchFriendRequests();
  }, [showModal]);

  const handleSelectRequest = (req) => setSelectedRequest(req);

  const handleAccept = async (req) => {
     const confirmed = window.confirm("Are you sure you want to accept this friend request?");
  if (!confirmed) return;
    try {
      const token = localStorage.getItem('token');
      await axios.put('http://localhost:8080/api/friend-requests', {
        requestId: req.id, approve: true
      }, {
        headers: { Authorization: `Bearer ${token}` },
      });
      closeModal();
    } catch (err) {
      alert("Failed to accept friend request");
    }
    setSelectedRequest(null);
  };

  const handleReject = async (req) => {
     const confirmed = window.confirm("Are you sure you want to reject this friend request?");
  if (!confirmed) return;
    try {
      const token = localStorage.getItem('token');
      await axios.put('http://localhost:8080/api/friend-requests', {
        requestId: req.id, approve: false
      }, {
        headers: { Authorization: `Bearer ${token}` },
      });
      closeModal();
    } catch (err) {
      alert("Failed to reject friend request");
    }
    setSelectedRequest(null);
  };

  return (
    <>
      <button onClick={showPendingRequests}>🤝 Pending Friend Requests</button>
      {showModal && (
        <div
          style={{
            position: 'fixed',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: 'rgba(0,0,0,0.5)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            zIndex: 1000,
          }}
        >
          <div
            style={{
              background: 'white',
              padding: '2rem',
              borderRadius: '10px',
              width: '80%',
              maxWidth: '500px',
              position: 'relative',
            }}
          >
            <button
              onClick={closeModal}
              style={{
                position: 'absolute',
                top: '10px',
                right: '10px',
                background: 'transparent',
                border: 'none',
                fontSize: '1.5rem',
                cursor: 'pointer',
              }}
            >
              &times;
            </button>
            <FriendRequestList friendRequests={requests} onRequestClick={handleSelectRequest} />
          </div>
        </div>
      )}

      {selectedRequest && (
        <FriendRequestModal
          request={selectedRequest}
          onAccept={handleAccept}
          onReject={handleReject}
          onClose={() => setSelectedRequest(null)}
        />
      )}
    </>
  );
};

export default PendingFriendRequestsSection;