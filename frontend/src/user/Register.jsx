import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Register(){
    const[username,setUsername] =useState("");
    const[password,setPassword]=useState("");
    const navigate=useNavigate();

    const handleRegister=()=>{
       try{
        axios.post('http://localhost:8080/api/users/sign-up',{
            username,password
        });
        navigate('/');
       }catch(err){
        alert("Registration failed");
       }
    }

    return (
    <div style={{ padding: 20 }}>
      <h2>Register</h2>
      <input placeholder="Username" value={username} onChange={e => setUsername(e.target.value)} /><br/>
      <input placeholder="Password" type="password" value={password} onChange={e => setPassword(e.target.value)} /><br/>
      <button onClick={handleRegister}>Register</button>
    </div>

    );
}
export default Register;