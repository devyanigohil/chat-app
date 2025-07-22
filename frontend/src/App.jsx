
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './user/Login';
import Register from './user/Register';
import ChatRoom from './chat/ChatRoom';
import ChatDashboard from './userdashboard/ChatDashBoard.jsx';
import HomePage from './HomePage';

import './App.css'

function App() {

   return (
   
     <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Register />} />
        <Route path="/chatroom" element={<ChatRoom />} />
        <Route path="/dashboard" element={<ChatDashboard/>}/>
      </Routes>
    </Router>
  );
}

export default App
