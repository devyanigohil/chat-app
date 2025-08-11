import { useState } from 'react';
import axios from '../api/axiosInstance'; 
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye,faEyeSlash  } from "@fortawesome/free-solid-svg-icons";

import './css/Login.css';


const eye = <FontAwesomeIcon icon={faEye} />;
const eyeSlash = <FontAwesomeIcon icon={faEyeSlash} />;

function Login({ onLogin }) {  // <-- Accept onLogin as prop

  const [username, setname] = useState('');
  const [password, setPassword] = useState('');
  const [passwordShown, setPasswordShown] = useState(false);
  const togglePasswordVisiblity = () => {
    setPasswordShown(passwordShown ? false : true);
  }
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const res = await axios.post('http://localhost:8080/api/users/loginuser', {
        username,
        password
      }, {
        headers: { "Content-Type": "application/json" },
        withCredentials: true  
      });

      localStorage.setItem('token',  res.data.accessToken); 
      localStorage.setItem('refreshToken', res.data.refreshToken);
      localStorage.setItem("username", username);

      onLogin?.(); // <-- Notify parent (like ChatRoom) if onLogin is passed
      navigate('/dashboard');

    } catch (err) {
      alert("Invalid Credentials");
    }
  }

  return (
    <>
      <div className="login-bg"></div>
      <button className="home-btn" onClick={() => navigate('/')}>Home</button>
      <div className="login-container">
        <h2 className="login-title">Enter your Credentials here</h2>
        <div className="login-field">
          <input className="login-input" placeholder="Username" value={username} onChange={e => setname(e.target.value)} />
        </div>
        <div className="login-field">
          <input className="login-input password"  type={passwordShown ? "text" : "password"} placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} />
            <i onClick={togglePasswordVisiblity}>{passwordShown?eye:eyeSlash}</i>{" "}
        </div>
        <button className="login-btn" onClick={handleLogin}>Login</button><br />
        Don't have an account? <button className="register-link-btn" onClick={() => navigate('/signup')}>register here </button>
      </div>
    </>
  );
}

export default Login;
