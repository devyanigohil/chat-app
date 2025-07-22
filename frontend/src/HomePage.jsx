import React from 'react';
import { useNavigate } from 'react-router-dom';
import './HomePage.css';

function HomePage() {
  const navigate = useNavigate();


  return (
    <>
    <div className="homepage-bg"> </div>
      <div className="homepage-container">
        <h1 className="homepage-title">Welcome to Chat Application</h1>
        <p className="homepage-desc">
          Connect, chat, and collaborate instantly!
        </p>
        <button
          className="homepage-btn login"
          onClick={() => navigate('/login')}
        >
          Login
        </button>
        <button
          className="homepage-btn register"
          onClick={() => navigate('/signup')}
        >
          Register
        </button>
      </div>
   </>
  );
}

export default HomePage;