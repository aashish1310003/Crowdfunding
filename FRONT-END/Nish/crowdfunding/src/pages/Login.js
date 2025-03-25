import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import "../styles/Login.css";
import { BASE_URL } from "../api/api";
import { jwtDecode } from "jwt-decode";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [otp, setOtp] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [step, setStep] = useState(1); // 1 = Login, 2 = OTP Verification
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    setIsLoading(true);

    try {
      const response = await axios.post(`${BASE_URL}/api/auth/login`, {
        email,
        password,
      });

      if (response.data.token) {
        // If token is received, store it and navigate to home
        const token = response.data.token;
        localStorage.setItem("token", token);

        const decoded = jwtDecode(token);
        localStorage.setItem("userId", decoded.userId);
        localStorage.setItem("email", decoded.sub);
        localStorage.setItem("name", decoded.name);
        localStorage.setItem("roles", decoded.roles);

        navigate("/home");
      } else {
        // If no token is received, move to OTP step
        setStep(2);
      }
    } catch (err) {
      setError(err.response?.data?.message || "Invalid email or password");
    } finally {
      setIsLoading(false);
    }
  };

  const handleVerifyOtp = async (e) => {
    e.preventDefault();
    setError("");
    setIsLoading(true);

    try {
      const response = await axios.post(`${BASE_URL}/api/auth/verify-otp`, {
        email,
        password: otp, // Sending OTP instead of password
      });

      if (response.data.token) {
        const token = response.data.token;
        localStorage.setItem("token", token);

        const decoded = jwtDecode(token);
        localStorage.setItem("userId", decoded.userId);
        localStorage.setItem("email", decoded.sub);
        localStorage.setItem("name", decoded.name);
        localStorage.setItem("roles", decoded.roles);

        navigate("/home");
      } else {
        setError("OTP verification failed.");
      }
    } catch (err) {
      setError(err.response?.data?.message || "Invalid OTP");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="portal-header">
        <h1 className="portal-title">Crowdfunding Portal</h1>
        <p className="portal-subtitle">
          Empower Innovation • Support Dreams • Build the Future
        </p>
      </div>

      <div className="login-container">
        <div className="login-card">
          {step === 1 ? (
            <>
              <h2 className="login-title">Welcome Back</h2>
              <p className="login-subtitle">
                Sign in to continue to your account
              </p>

              <form onSubmit={handleLogin} className="login-form">
                <div className="form-group">
                  <label htmlFor="email">Email Address</label>
                  <input
                    type="email"
                    id="email"
                    placeholder="Enter your email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="password">Password</label>
                  <input
                    type="password"
                    id="password"
                    placeholder="Enter your password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                  />
                </div>

                <button type="submit" disabled={isLoading}>
                  {isLoading ? <span className="spinner"></span> : "Sign In"}
                </button>
              </form>
            </>
          ) : (
            <>
              <h2 className="login-title">Enter OTP</h2>
              <p className="login-subtitle">OTP sent to {email}</p>

              <form onSubmit={handleVerifyOtp} className="login-form">
                <div className="form-group">
                  <label htmlFor="otp">OTP</label>
                  <input
                    type="text"
                    id="otp"
                    placeholder="Enter OTP"
                    value={otp}
                    onChange={(e) => setOtp(e.target.value)}
                    required
                  />
                </div>

                <button type="submit" disabled={isLoading}>
                  {isLoading ? <span className="spinner"></span> : "Verify OTP"}
                </button>
              </form>

              <button
                className="resend-otp"
                onClick={handleLogin}
                disabled={isLoading}>
                Resend OTP
              </button>
            </>
          )}

          {error && <p className="error-message">{error}</p>}

          {step === 1 && (
            <p className="register-link">
              Don't have an account? <Link to="/register">Register</Link>
            </p>
          )}
        </div>
      </div>
    </div>
  );
};

export default Login;
