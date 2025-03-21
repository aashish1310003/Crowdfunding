import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import axiosInstance from "../middleware/axiosInstance";
import "../styles/myprojects.css";
import { CircularProgress } from "@mui/material";

const MyProjects = () => {
  const [projects, setProjects] = useState([]);
  const [selectedStatus, setSelectedStatus] = useState("APPROVED");
  const [userId, setUserId] = useState(null);
  const [loading, setLoading] = useState(true); // Add a loading state
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decoded = jwtDecode(token);
        setUserId(decoded.userId);
      } catch (error) {
        console.error("Invalid token", error);
      }
    }
  }, []);

  useEffect(() => {
    if (!userId) return;

    setLoading(true); // Set loading to true before fetching data
    axiosInstance
      .get("/projects/admin/evaluated")
      .then(({ data }) => {
        setProjects(Array.isArray(data) ? data : []);
      })
      .catch((error) => console.error("Error fetching projects:", error))
      .finally(() => setLoading(false)); // Set loading to false after fetching
  }, [userId]);

  const filteredProjects = projects.filter(
    (project) => project.status === selectedStatus
  );

  const statuses = ["APPROVED", "REJECTED"];

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
      <div className="project-list">
        {loading ? ( // Show loading indicator if data is being fetched
          <div className="loading-spinner">
            <CircularProgress />{" "}
          </div>
        ) : filteredProjects.length > 0 ? ( // Show projects if data is available
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
          // Show message if no projects are found
          <p>No {selectedStatus.toLowerCase()} projects found.</p>
        )}
      </div>
    </div>
  );
};

export default MyProjects;
