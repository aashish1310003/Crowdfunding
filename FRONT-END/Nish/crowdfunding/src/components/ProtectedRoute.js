import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../AuthContext";

const ProtectedRoute = ({ adminOnly = false, studentOnly = false }) => {
  const { isAuthenticated, isAdmin, isStudent } = useAuth();

  console.log("Auth Debug: ", { isAuthenticated, isAdmin, isStudent });

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (adminOnly && !isAdmin) {
    return <Navigate to="/home" replace />;
  }

  if (studentOnly && !isStudent) {
    return <Navigate to="/home" replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
