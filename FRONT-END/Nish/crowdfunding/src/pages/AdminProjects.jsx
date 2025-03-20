import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { BASE_URL } from "../api/api";
import "../styles/myprojects.css";

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

    fetch(`${BASE_URL}/projects/admin`)
      .then((response) => response.json())
      .then((data) => {
        setProjects(Array.isArray(data) ? data : [data]);
      })
      .catch((error) => console.error("Error fetching projects:", error));
  }, [userId]);

  const filteredProjects = projects.filter(
    (project) => project.status === selectedStatus
  );

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
      <div className="project-list">
        {filteredProjects.length > 0 ? (
          filteredProjects.map((project) => (
            <div
              key={project.projectId}
              className="project-card"
              onClick={() =>
                navigate(`/admin/project-details/${project.projectId}`)
              } // Navigate to project details
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
          <p>No {selectedStatus.toLowerCase()} projects found.</p>
        )}
      </div>
    </div>
  );
};

export default MyProjects;
