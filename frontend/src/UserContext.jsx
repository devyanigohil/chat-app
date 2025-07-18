import React, { createContext, useState } from 'react';

export const UserContext = createContext();

export const UserProvider = ({ children }) => {
  const [username, setUsername] = useState('');
  const [roomName, setRoomName] = useState('');

  return (
    <UserContext.Provider value={{ username, setUsername, roomName, setRoomName }}>
      {children}
    </UserContext.Provider>
  );
};
