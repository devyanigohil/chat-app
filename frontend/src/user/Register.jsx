import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './css/Register.css';

function Register() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [otp, setOtp] = useState("");
  const [otpSent, setOtpSent] = useState(false);
  const navigate = useNavigate();

  const handleSendOtp = async () => {
    try {
      await axios.post('http://localhost:8080/api/users/send-otp', { email });
      setOtpSent(true);
      alert("OTP sent to your email!");
    } catch (err) {
      alert("Failed to send OTP");
    }
  };

  const handleRegister = async () => {
    try {
      await axios.post('http://localhost:8080/api/users/sign-up', {
        name,
        email,
        username,
        password,
        otp
      });
      alert("Registration successful!");
      navigate('/');
    } catch (err) {
      alert("Registration failed");
    }
  };

  return (
    <>
    <div className="register-bg"></div>
      <div className="register-container">
        <h2 className="register-title">Register</h2>
        <div className="register-field">
          <input
            className="register-input"
            placeholder="Name"
            value={name}
            onChange={e => setName(e.target.value)}
          />
        </div>
        <div className="register-field email-field">
          <input
            className="register-input"
            placeholder="Email"
            type="email"
            value={email}
            onChange={e => setEmail(e.target.value)}
          />
          <button
            className="register-btn otp-btn"
            onClick={handleSendOtp}
            disabled={!email || otpSent}
          >
            Send OTP
          </button>
        </div>
        <div className="register-field email-field">
          <input
            className="register-input"
            placeholder="OTP"
            value={otp}
            onChange={e => setOtp(e.target.value)}
          />
           <button
            className="register-btn otp-btn"
           // onClick={handleVerifyOtp}
            disabled={!otp}
          >
            Verify OTP
          </button>
        </div>
        <div className="register-field">
          <input
            className="register-input"
            placeholder="Username"
            value={username}
            onChange={e => setUsername(e.target.value)}
          />
        </div>
        <div className="register-field">
          <input
            className="register-input"
            placeholder="Password"
            type="password"
            value={password}
            onChange={e => setPassword(e.target.value)}
          />
        </div>
        <button className="register-btn main-btn" onClick={handleRegister}>
          Register
        </button>
      </div>
    
    </>
  );
}

export default Register;