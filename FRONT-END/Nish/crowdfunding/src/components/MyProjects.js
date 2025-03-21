import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { BASE_URL } from "../api/api";
import "../styles/myprojects.css";
import axiosInstance from "../middleware/axiosInstance";
import { CircularProgress } from "@mui/material";

const MyProjects = () => {
  const [projects, setProjects] = useState([]);
  const [selectedStatus, setSelectedStatus] = useState("CREATED");
  const [userId, setUserId] = useState(null);
  const navigate = useNavigate(); // Initialize navigate function

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

    axiosInstance
      .get(`${BASE_URL}/projects/project/by/user/${userId}`)
      .then((response) => {
        // Access the data directly from the response
        const data = response.data;
        setProjects(Array.isArray(data) ? data : [data]);
      })
      .catch((error) => console.error("Error fetching projects:", error));
  }, [userId]);

  const filteredProjects = projects.filter(
    (project) => project.status === selectedStatus
  );

  const statuses = ["CREATED", "PENDING", "APPROVED", "REJECTED"];

  return (
    <div className="myprojects-container">
      <h1>My Projects</h1>
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
        {filteredProjects.length > 0 ? (
          filteredProjects.map((project) => (
            <div
              key={project.projectId}
              className="project-card"
              onClick={() => navigate(`/project/${project.projectId}`)} // Navigate to project details
              style={{ cursor: "pointer" }} // Indicate clickable behavior
            >
              <h3>{project.title}</h3>
              <p>{project.description}</p>
              <p>
                <strong>Status:</strong> {project.status}
              </p>
            </div>
          ))
        ) : (
          <p>
            <CircularProgress />{" "}
          </p>
        )}
      </div>
    </div>
  );
};

export default MyProjects;
