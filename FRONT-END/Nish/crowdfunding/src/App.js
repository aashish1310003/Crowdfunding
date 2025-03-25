import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  useLocation,
} from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
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
import AdminProjects from "./pages/AdminProjects";
import AdminProjectDetails from "./pages/AdminProjectDetails";
import ProtectedRoute from "./components/ProtectedRoute";
import AdminEvaluated from "./pages/AdminEvaluated";
import AchievedProjects from "./pages/AchievedProjects";

function App() {
  return (
    <AuthProvider>
      <Router>
        {/* Global Toast Container for error notifications */}
        <ToastContainer
          position="top-right"
          autoClose={5000}
          hideProgressBar={false}
          closeOnClick={false}
          pauseOnHover={true}
          draggable={true}
          theme="light"
        />
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

        {/* Protected Routes (Require Login) */}
        <Route element={<ProtectedRoute />}>
          <Route path="/payment/:id" element={<PaymentPage />} />
          <Route path="/my-projects" element={<MyProjects />} />
          <Route path="/payment-success" element={<PaymentSuccess />} />
          <Route path="/payment-failure" element={<PaymentFailure />} />
        </Route>

        <Route element={<ProtectedRoute studentOnly={true} />}>
          <Route path="/create-project" element={<CreateProject />} />
          <Route path="/update/:projectId" element={<UpdateProject />} />
        </Route>

        {/* Admin Routes (Require Admin Role) */}
        {/* <Route element={<ProtectedRoute adminOnly={true} />}> */}
        <Route element={<ProtectedRoute />}>
          <Route path="/admin/evaluate/projects" element={<AdminProjects />} />
          <Route
            path="/admin/evaluated/projects"
            element={<AdminEvaluated />}
          />
          <Route
            path="/admin/project-details/:projectId"
            element={<AdminProjectDetails />}
          />
          <Route
            path="/admin/achieve/projects"
            element={<AchievedProjects />}
          />
        </Route>
      </Routes>
    </>
  );
}

export default App;
