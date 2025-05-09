import React, { useState } from 'react';
import PasswordForm from './PasswordForm';

const PasswordDetail = ({ password, onUpdate, onCancel }) => {
    const [isEditing, setIsEditing] = useState(false);

    const handleUpdate = (updatedPassword) => {
        // Garante que estamos enviando a senha descriptografada
        onUpdate(password.id, {
            ...updatedPassword,
            encryptedPassword: updatedPassword.password // Assume que o form usa campo "password"
        });
        setIsEditing(false);
    };

    return (
        <div>
            {isEditing ? (
                <PasswordForm
                    initialData={{
                        ...password,
                        password: password.encryptedPassword // Passa a senha descriptografada
                    }}
                    onSubmit={handleUpdate}
                    onCancel={() => setIsEditing(false)}
                />
            ) : (
                <div>
                    <h2>{password.serviceName}</h2>
                    <p>Usuário: {password.username}</p>
                    <p>Email: {password.email}</p>
                    <p>Senha: {password.encryptedPassword}</p> {/* Já descriptografada */}
                    <p>Notas: {password.notes}</p>
                    <button onClick={() => setIsEditing(true)}>Editar</button>
                    <button onClick={onCancel}>Voltar</button>
                </div>
            )}
        </div>
    );
};

export default PasswordDetail;