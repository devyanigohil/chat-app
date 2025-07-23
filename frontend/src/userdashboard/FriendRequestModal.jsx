import React from 'react';
import './css/JoinRequestModal.css';

const FriendRequestModal = ({ request, onAccept, onReject, onClose }) => {
  if (!request) return null;

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h3>Friend Request Details</h3>
        <p><strong>From:</strong> { request.sender}</p>
        <p><strong>Message:</strong> <em>{request.msg || <i>No message</i>}</em></p>
        <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '1rem' }}>
          <button className="accept-btn" onClick={() => onAccept(request)}>✅ Accept</button>
          <button className="reject-btn" onClick={() => onReject(request)}>❌ Reject</button>
        </div>
        <button className="close-btn" onClick={onClose}>Close</button>
      </div>
    </div>
  );
};

export default FriendRequestModal;