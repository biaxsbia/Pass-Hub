import React, { useState } from "react";
import axios from "axios";
import { QRCodeSVG } from "qrcode.react";

export default function Register() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [otpUrl, setOtpUrl] = useState(null);
  const [totpSecret, setTotpSecret] = useState(null);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleRegister = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");
    setOtpUrl(null);

    try {
      const response = await axios.post("http://localhost:8080/auth/register", {
        email,
        password,
      });

      const data = response.data;
      setSuccess("Usuário registrado com sucesso!");
      setOtpUrl(data.otpAuthUrl);
      setTotpSecret(data.totpSecret);
    } catch (err) {
      setError(err.response?.data || "Erro ao registrar");
    }
  };

  return (
    <div style={{ maxWidth: 400, margin: "auto", padding: 20 }}>
      <h2>Cadastro</h2>
      <form onSubmit={handleRegister}>
        <div>
          <label>Email:</label>
          <br />
          <input
            type="email"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>
        <div>
          <label>Senha:</label>
          <br />
          <input
            type="password"
            required
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <button type="submit" style={{ marginTop: 10 }}>
          Registrar
        </button>
      </form>

      {error && <p style={{ color: "red", marginTop: 10 }}>{error}</p>}
      {success && (
        <div style={{ color: "green", marginTop: 10 }}>
          <p>{success}</p>
          <p>
            Para ativar sua autenticação em dois fatores, escaneie o QR Code abaixo com o Google Authenticator. <p></p>Quando terminar, clique em <a href="/login">"Fazer login"</a>
          </p>
        </div>
      )}

      {otpUrl && (
        <div style={{ marginTop: 20 }}>
          <h3>Escaneie o QR Code no Google Authenticator:</h3>
          <QRCodeSVG value={otpUrl} size={200} />
          <p style={{ marginTop: 10 }}>
            <strong>Código manual (caso necessário):</strong>
          </p>
          <code>{totpSecret}</code>
        </div>
      )}
    </div>
  );
}
