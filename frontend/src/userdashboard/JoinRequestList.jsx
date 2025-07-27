import React from 'react';
//import './JoinRequestList.css';

const JoinRequestList = ({ joinRequests, onRequestClick }) => {
    return (
 <div className="join-requests" style={{ marginTop: '2rem' }}>
      <h3>🔔 Pending Join Requests</h3>
   {(!joinRequests || joinRequests.length === 0) ? (
        <p style={{ fontStyle: 'italic', color: '#777' }}>
          No pending requests.
        </p>
      ) : (

   
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {joinRequests.map((req, index) => (
          <li
            key={index}
            style={{
              cursor: 'pointer',
              padding: '1rem',
              border: '1px solid #ccc',
              marginBottom: '10px',
              borderRadius: '8px',
              backgroundColor: '#f5f5f5',
            }}
            onClick={() => onRequestClick(req)}
          >
            <strong>{req.roomname}</strong> – {req.msg}
          </li>
        ))}
      </ul>)}
    </div>
  );
};

export default JoinRequestList;
