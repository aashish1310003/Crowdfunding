import React, { createContext, useState } from "react";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const defaultUser = {
    userId: 1,
    name: "nish",
    email: "nish.it20@gmail.com",
    password: "1234",
    role: "DONOR",
  };

  const [user, setUser] = useState(defaultUser); // Set default user

  const login = (userData) => {
    setUser(userData);
    localStorage.setItem("user", JSON.stringify(userData));
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem("user");
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export default AuthContext;
