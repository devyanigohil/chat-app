import React from 'react';
import './css/JoinRequestModal.css';

const JoinRequestModal = ({ request, onAccept, onReject, onClose }) => {
  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h3>Join Request Details</h3>
        <p><strong>Room:</strong> {request.roomname}</p>
        <p><strong>Admin:</strong> {request.admin}</p>
        <p><strong>Description:</strong> {request.roomDescription}</p>
        <p><strong>Message:</strong> <em>{request.msg}</em></p>

        <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '1rem' }}>
          <button className="accept-btn" onClick={() => onAccept(request)}>✅ Accept</button>
          <button className="reject-btn" onClick={() => onReject(request)}>❌ Reject</button>
        </div>
        <button className="close-btn" onClick={onClose}>Close</button>
      </div>
    </div>
  );
};

export default JoinRequestModal;
