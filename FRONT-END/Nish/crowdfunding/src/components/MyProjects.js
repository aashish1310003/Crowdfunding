import React, { useState, useEffect } from "react";
import { jwtDecode } from "jwt-decode";
import { BASE_URL } from "../api/api";
// Import jwtDecode to decode token
import "../styles/myprojects.css";

const MyProjects = () => {
  const [projects, setProjects] = useState([]); // Initialize state for projects
  const [selectedStatus, setSelectedStatus] = useState("CREATED"); // State for selected status
  const [userId, setUserId] = useState(null); // State to store user ID

  useEffect(() => {
    // Retrieve token from localStorage and decode user ID
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decoded = jwtDecode(token);
        setUserId(decoded.userId); // Extract user ID from token
      } catch (error) {
        console.error("Invalid token", error);
      }
    }
  }, []);

  useEffect(() => {
    if (!userId) return;

    // Fetch only projects of the logged-in user
    fetch(`${BASE_URL}/projects/project/by/user/${userId}`)
      .then((response) => response.json())
      .then((data) => {
        setProjects(Array.isArray(data) ? data : [data]);
      })
      .catch((error) => console.error("Error fetching projects:", error));
  }, [userId]);

  // Function to filter projects by selected status
  const filteredProjects = projects.filter(
    (project) => project.status === selectedStatus
  );

  // Define different statuses
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
            <div key={project.projectId} className="project-card">
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
