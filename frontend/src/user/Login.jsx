import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Login(){

  const [username, setname] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin= async () =>{
    try{
        const res = await axios.post('http://localhost:8080/api/users/loginuser', {
        username,
        password},{
        headers: { "Content-Type": "application/json" },
        withCredentials: true  
      });
        const actualJwtString = res.data.token;
      localStorage.setItem('token', actualJwtString); // ✅ only the token string // store JWT
      localStorage.setItem("username", username);
      navigate('/dashboard'); // navigate to chat
    }
    catch(err){
        alert("Invalid Credentials");
    }
  }


    return (
    <div style={{ padding: 20 }}>
      <h2>Login</h2>
      <input placeholder="Username" value={username} onChange={e => setname(e.target.value)} /><br/>
      <input placeholder="Password" type="password" value={password} onChange={e => setPassword(e.target.value)} /><br/>
      <button onClick={handleLogin}>Login</button>
    </div>
  );
}

export default Login;