import React, { useEffect, useState } from 'react';
import axios from 'axios';
import JoinRequestList from './JoinRequestList';
import JoinRequestModal from './JoinRequestModal';

const PendingRequestsSection = () => {
  const [requests, setRequests] = useState([]);
  const [selectedRequest, setSelectedRequest] = useState(null);
    const [showModal, setShowModal] = useState(false);

  const showPendingRequests = () => {
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
  };

  useEffect(() => {
    const fetchJoinRequests = async () => {
      try {
        const token = localStorage.getItem('token');
        const res = await axios.get('http://localhost:8080/api/join-requests', {
          headers: { Authorization: `Bearer ${token}` },
        });
        setRequests(res.data);
      } catch (err) {
        console.error('Error fetching requests', err);
      }
    };

    fetchJoinRequests();
  }, [showModal]);

  const handleSelectRoom = (req) => {
    // const req = requests.find(r => r.roomname === roomName);
    setSelectedRequest(req);
  };

  const handleAccept = async (req) => {
     try{
        const token = localStorage.getItem('token');
        axios.put('http://localhost:8080/api/join-requests',{
          requestId: req.id,approve:true
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
      closeModal();
       }catch(err){
        alert("Registration failed");
       }
    

    setSelectedRequest(null);
  };

  const handleReject = async (req) => {
     try{
        const token = localStorage.getItem('token');
        axios.put('http://localhost:8080/api/join-requests',{
          requestId: req.id,approve:false
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
      closeModal();
       }catch(err){
        alert("Registration failed");
       }
    

    setSelectedRequest(null);
  };


  return (
    <>
    <button onClick={showPendingRequests}>🔔Pending Join Requests</button>
    
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
           <JoinRequestList joinRequests={requests} onRequestClick={handleSelectRoom} />
          </div>
        </div>
      )}
        
      {selectedRequest && (
        <JoinRequestModal
          request={selectedRequest}
          onAccept={handleAccept}
          onReject={handleReject}
          onClose={() => setSelectedRequest(null)}
        />
      )}
    </>
  );
};

export default PendingRequestsSection;
