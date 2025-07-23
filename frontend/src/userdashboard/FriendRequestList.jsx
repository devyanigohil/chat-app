import React from 'react';

const FriendRequestList = ({ friendRequests, onRequestClick }) => {
  return (
    <div className="friend-requests" style={{ marginTop: '2rem' }}>
      <h3>🤝 Pending Friend Requests</h3>
      {(!friendRequests || friendRequests.length === 0) ? (
        <p style={{ fontStyle: 'italic', color: '#777' }}>
          No pending friend requests.
        </p>
      ) : (
        <ul style={{ listStyle: 'none', padding: 0 }}>
          {friendRequests.map((req, index) => (
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
              <strong>{req.sender}</strong>
              {req.msg && <> – {req.msg}</>}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default FriendRequestList;