import React, { useState, useEffect } from 'react';
import PasswordList from './components/PasswordList';
import Login from './components/Login';
import './styles.css';

function App() {
  const [loggedIn, setLoggedIn] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('token');
    setLoggedIn(!!token);
  }, []);

  return (
    <div className="App">
      {loggedIn ? (
        <PasswordList />
      ) : (
        <Login onLoginSuccess={() => setLoggedIn(true)} />
      )}
    </div>
  );
}

export default App;