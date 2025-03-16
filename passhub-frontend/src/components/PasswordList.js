import React, { useEffect, useState } from 'react';
import api from '../services/api';
import PasswordForm from './PasswordForm';
import PasswordDetail from './PasswordDetail';

const PasswordList = () => {
    const [passwords, setPasswords] = useState([]);
    const [selectedPassword, setSelectedPassword] = useState(null);
    const [showForm, setShowForm] = useState(false);

    useEffect(() => {
        fetchPasswords();
    }, []);

    const fetchPasswords = async () => {
        try {
            const response = await api.get();
            setPasswords(response.data);
        } catch (error) {
            console.error('Erro ao buscar senhas:', error);
        }
    };

    const handleViewDetails = async (id) => {
        try {
            const response = await api.get(`/${id}`); 
            setSelectedPassword(response.data);
        } catch (error) {
            console.error('Erro ao buscar detalhes da senha:', error);
        }
    };

    const handleAddPassword = async (password) => {
        try {
            await api.post('', password);
            fetchPasswords();
            setShowForm(false);
        } catch (error) {
            console.error('Erro ao adicionar senha:', error);
        }
    };

    const handleUpdatePassword = async (id, password) => {
        try {
            await api.put(`/${id}`, password);
            fetchPasswords();
            setSelectedPassword(null);
        } catch (error) {
            console.error('Erro ao atualizar senha:', error);
        }
    };

    const handleDeletePassword = async (id) => {
        try {
            await api.delete(`/${id}`);
            fetchPasswords();
        } catch (error) {
            console.error('Erro ao excluir senha:', error);
        }
    };

    return (
        <div>
            <h1>Gerenciador de Senhas</h1>
            <button onClick={() => setShowForm(!showForm)}>
                {showForm ? 'Cancelar' : 'Adicionar Senha'}
            </button>

            {showForm && (
                <PasswordForm
                    onSubmit={handleAddPassword}
                    onCancel={() => setShowForm(false)}
                />
            )}

            {selectedPassword && (
                <PasswordDetail
                    password={selectedPassword}
                    onUpdate={handleUpdatePassword}
                    onCancel={() => setSelectedPassword(null)}
                />
            )}

            <ul>
            <li style={{ fontWeight: 'bold', display: 'flex', justifyContent: 'space-between', padding: '10px', borderBottom: '1px solid #ccc' }}>
                <span>Serviço</span>
        <span>Usuário</span>
        <span>Ações</span>
                </li>
                {passwords.map((password) => (
                    <li key={password.id} style={{ display: 'flex', justifyContent: 'space-between', padding: '10px', borderBottom: '1px solid #eee' }}>
                        <strong>{password.serviceName}</strong> {password.username}
                        <button onClick={() => handleViewDetails(password.id)}>Ver detalhes</button>
                        <button onClick={() => handleDeletePassword(password.id)}>Excluir</button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default PasswordList;