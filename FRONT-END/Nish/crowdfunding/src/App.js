import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  useLocation,
} from "react-router-dom";
import HomePage from "./pages/HomePage";
import ProjectDetails from "./pages/ProjectDetails";
import Navbar from "./components/Navbar";
import { AuthProvider } from "./AuthContext";
import MyProjects from "./components/MyProjects";
import CreateProject from "./components/CreateProject";
import Register from "./pages/Register";
import Login from "./pages/Login";
import PaymentPage from "./pages/PaymentPage";
import PaymentSuccess from "./pages/PaymentSuccess";
import PaymentFailure from "./pages/PaymentFailure";
import UpdateProject from "./components/updateProject";

function App() {
  return (
    <AuthProvider>
      <Router>
        <MainLayout />
      </Router>
    </AuthProvider>
  );
}

function MainLayout() {
  const location = useLocation();
  return (
    <>
      <Routes>
        <Route path="/" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/project/:id" element={<ProjectDetails />} />
        <Route path="/payment/:id" element={<PaymentPage />} />
        <Route path="/my-projects" element={<MyProjects />} />
        <Route path="/create-project" element={<CreateProject />} />
        <Route path="/update/:projectId" element={<UpdateProject />} />
        <Route path="/payment-success" element={<PaymentSuccess />} />
        <Route path="/payment-failure" element={<PaymentFailure />} />
      </Routes>
    </>
  );
}

export default App;
