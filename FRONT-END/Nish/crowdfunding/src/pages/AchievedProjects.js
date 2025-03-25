import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import axiosInstance from "../middleware/axiosInstance";
import "../styles/myprojects.css";
import { CircularProgress } from "@mui/material";

const MyProjects = () => {
  const [projects, setProjects] = useState([]);
  const [selectedStatus, setSelectedStatus] = useState("REACHED");
  const [userId, setUserId] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decoded = jwtDecode(token);
        setUserId(decoded.userId);
      } catch (error) {
        console.error("Invalid token", error);
        localStorage.removeItem("token");
        navigate("/login");
      }
    } else {
      navigate("/login");
    }
  }, [navigate]);

  useEffect(() => {
    if (!userId) return;

    setLoading(true);
    const endpoint =
      selectedStatus === "REACHED"
        ? "/projects/goal/reached"
        : "/projects/goal/not-reached";

    axiosInstance
      .get(endpoint)
      .then(({ data }) => {
        console.log("Fetched projects:", data);
        setProjects(Array.isArray(data) ? data : []);
      })
      .catch((error) => {
        console.error("Error fetching projects:", error);
        if (error.response?.status === 401) {
          localStorage.removeItem("token");
          navigate("/login");
        }
      })
      .finally(() => setLoading(false));
  }, [userId, selectedStatus, navigate]);

  const statuses = ["REACHED", "NOT REACHED"];

  return (
    <div className="myprojects-container">
      <h1>Project Goal Status</h1>
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

      <div className="project-list">
        {loading ? (
          <CircularProgress />
        ) : projects.length > 0 ? (
          projects.map((project) => (
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
                <strong>Goal Status:</strong> {selectedStatus}
              </p>
            </div>
          ))
        ) : (
          <p>No projects found for {selectedStatus.toLowerCase()} goal.</p>
        )}
      </div>
    </div>
  );
};

export default MyProjects;
