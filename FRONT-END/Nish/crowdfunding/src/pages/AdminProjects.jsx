import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import axiosInstance from "../middleware/axiosInstance";
import "../styles/myprojects.css";
import { CircularProgress } from "@mui/material"; // Import CircularProgress

const MyProjects = () => {
  const [projects, setProjects] = useState([]);
  const [selectedStatus, setSelectedStatus] = useState("CREATED");
  const [userId, setUserId] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  // ✅ Handle Token and User Authentication
  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decoded = jwtDecode(token);
        setUserId(decoded.userId); // Update userId
      } catch (error) {
        console.error("Invalid token", error);
        localStorage.removeItem("token"); // Clear invalid token
        navigate("/login"); // Redirect to login if token is invalid
      }
    } else {
      navigate("/login"); // Redirect if no token is found
    }
  }, [navigate]);

  // ✅ Fetch User's Projects
  useEffect(() => {
    if (!userId) return; // Ensure userId is set before making the API call

    setLoading(true);
    axiosInstance
      .get("/projects/admin")
      .then(({ data }) => {
        console.log("Fetched projects:", data); // Debug API response
        setProjects(Array.isArray(data) ? data : []);
      })
      .catch((error) => {
        console.error("Error fetching projects:", error);
        if (error.response?.status === 401) {
          localStorage.removeItem("token"); // Remove expired token
          navigate("/login"); // Redirect to login
        }
      })
      .finally(() => setLoading(false));
  }, [userId, navigate]);

  // ✅ Filter projects by selected status
  const filteredProjects = projects.filter(
    (project) => project.status === selectedStatus
  );

  // ✅ Status options
  const statuses = ["CREATED", "PENDING"];

  return (
    <div className="myprojects-container">
      <h1>Evaluate these Projects</h1>
      <div className="status-buttons">
        {statuses.map((status) => (
          <button
            key={status}
            className={`status-button ${
              selectedStatus === status ? "active" : ""
            }`}
            onClick={() => setSelectedStatus(status)}>
            {status}
          </button>
        ))}
      </div>

      {/* ✅ Show loading spinner while fetching data */}
      <div className="project-list">
        {loading ? (
          <CircularProgress />
        ) : filteredProjects.length > 0 ? (
          filteredProjects.map((project) => (
            <div
              key={project.projectId}
              className="project-card"
              onClick={() =>
                navigate(`/admin/project-details/${project.projectId}`)
              }
              style={{ cursor: "pointer" }}>
              <h3>{project.title}</h3>
              <p>{project.description}</p>
              <p>
                <strong>Status:</strong> {project.status}
              </p>
            </div>
          ))
        ) : (
          <p>No {selectedStatus.toLowerCase()} projects found.</p>
        )}
      </div>
    </div>
  );
};

export default MyProjects;
