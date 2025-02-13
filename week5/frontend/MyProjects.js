import React, { useState, useEffect } from "react";
import "../styles/styles.css";

const MyProjects = () => {
  const [projects, setProjects] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");

  const loggedInUserId = 1; // Replace with actual logged-in user ID

  useEffect(() => {
    fetch("http://localhost:5000/projects")
      .then((response) => response.json())
      .then((data) => {
        const userProjects = data.filter(
          (project) => project.user?.userId === loggedInUserId
        );
        setProjects(userProjects);
      })
      .catch((error) => console.error("Error fetching projects:", error));
  }, []);

  const filteredProjects = projects.filter((project) =>
    project.title.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="myprojects-container">
      <h1>My Projects</h1>

      <input
        type="text"
        placeholder="Search my projects..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className="search-bar"
      />

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
        <p>No projects found.</p>
      )}
    </div>
  );
};

export default MyProjects;
