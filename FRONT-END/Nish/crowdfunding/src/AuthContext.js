import React, { createContext, useContext, useState, useEffect } from "react";
import { jwtDecode } from "jwt-decode";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true); // Add a loading state

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decoded = jwtDecode(token);
        setUser({
          userId: decoded.userId,
          email: decoded.sub,
          name: decoded.name,
          role: Array.isArray(decoded.roles) ? decoded.roles[0] : decoded.roles, // Ensure it's a string
        });
      } catch (error) {
        console.error("Invalid token:", error);
        localStorage.removeItem("token");
      }
    }
    setLoading(false); // Set loading to false after initialization
  }, []); // Runs only once on mount

  const login = (token) => {
    localStorage.setItem("token", token);
    try {
      const decoded = jwtDecode(token);
      setUser({
        userId: decoded.userId,
        email: decoded.sub,
        name: decoded.name,
        role: Array.isArray(decoded.roles) ? decoded.roles[0] : decoded.roles,
      });
    } catch (error) {
      console.error("Invalid token:", error);
      logout();
    }
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem("token");
  };

  const isAuthenticated = !!user;
  const isAdmin = user?.role === "ADMIN";
  const isStudent = user?.role === "STUDENT";

  // Only render children when loading is false
  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        logout,
        isAuthenticated,
        isAdmin,
        isStudent,
        loading,
      }}>
      {!loading && children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
