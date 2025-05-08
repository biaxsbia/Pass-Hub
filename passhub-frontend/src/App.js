import React, { useState } from 'react';
import Login from './components/Login';
import PasswordList from './components/PasswordList';

const App = () => {
    const [token, setToken] = useState(localStorage.getItem('token'));

    const handleLogin = (token) => {
        localStorage.setItem('token', token);
        setToken(token);
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        setToken(null);
    };

    return (
        <div>
            {token ? (
                <div>
                    <button onClick={handleLogout}>Logout</button>
                    <PasswordList token={token} />
                </div>
            ) : (
                <Login onLogin={handleLogin} />
            )}
        </div>
    );
};

export default App;