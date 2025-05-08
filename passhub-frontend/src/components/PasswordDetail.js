import React, { useState } from 'react';
import PasswordForm from './PasswordForm';

const PasswordDetail = ({ password, onUpdate, onCancel }) => {
    const [isEditing, setIsEditing] = useState(false);

    const handleUpdate = (updatedPassword) => {
        onUpdate(password.id, updatedPassword);
        setIsEditing(false);
    };

    return (
        <div>
            {isEditing ? (
                <PasswordForm
                    initialData={password}
                    onSubmit={handleUpdate}
                    onCancel={() => setIsEditing(false)}
                />
            ) : (
                <div>
                    <h2>{password.serviceName}</h2>
                    <p>Usuário: {password.username}</p>
                    <p>Email: {password.email}</p>
                    <p>Senha: {password.encryptedPassword}</p>
                    <p>Notas: {password.notes}</p>
                    <button onClick={() => setIsEditing(true)}>Editar</button>
                    <button onClick={onCancel}>Voltar</button>
                </div>
            )}
        </div>
    );
};

export default PasswordDetail;