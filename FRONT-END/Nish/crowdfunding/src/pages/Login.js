import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../styles/Login.css";
import { BASE_URL } from "../api/api";
import { jwtDecode } from "jwt-decode"; // Correct import
import axiosInstance from "../middleware/axiosInstance";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const response = await axiosInstance.post(`${BASE_URL}/api/auth/login`, {
        email,
        password,
      });

      const token = response.data;
      console.log("Token:", token);
      localStorage.setItem("token", token);

      // Decode token correctly
      const decoded = jwtDecode(token);
      console.log("Decoded Token:", decoded);

      // Store user data in localStorage
      localStorage.setItem("userId", decoded.userId);
      localStorage.setItem("email", decoded.sub);
      localStorage.setItem("name", decoded.name);
      localStorage.setItem("roles", decoded.roles);

      navigate("/home");
    } catch (err) {
      setError("Invalid email or password");
    }
  };

  return (
    <div className="login-container">
      <h2>Login</h2>
      <form onSubmit={handleLogin} className="login-form">
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Login</button>
        {error && <p className="error-message">{error}</p>}
      </form>
    </div>
  );
};

export default Login;
