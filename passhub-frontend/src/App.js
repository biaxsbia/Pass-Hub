import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import PasswordList from './components/PasswordList';
import Login from './components/Login';
import Register from './components/Register';
import './styles.css';

function App() {
  const [loggedIn, setLoggedIn] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('token');
    setLoggedIn(!!token);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('token');
    setLoggedIn(false);
  };

  return (
    <Router>
      {loggedIn && (
        <button
          onClick={handleLogout}
          className="logout-button"
          style={{ position: 'absolute', top: 10, right: 10 }}
        >
          Sair
        </button>
      )}

      <div className="App">
        <Routes>
          {/* Página inicial redireciona conforme login */}
          <Route path="/" element={loggedIn ? <Navigate to="/passwords" /> : <Navigate to="/login" />} />

          {/* Login */}
          <Route
            path="/login"
            element={
              loggedIn ? <Navigate to="/passwords" /> : <Login onLoginSuccess={() => setLoggedIn(true)} />
            }
          />

          {/* Registro */}
          <Route
            path="/register"
            element={loggedIn ? <Navigate to="/passwords" /> : <Register />}
          />

          {/* Lista de senhas */}
          <Route
            path="/passwords"
            element={loggedIn ? <PasswordList /> : <Navigate to="/login" />}
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
