import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { FaUserCircle } from "react-icons/fa"; // Import profile icon
import Navbar from "../components/Navbar";
import "../styles/styles.css";
import { BASE_URL } from "../api/api";

const HomePage = () => {
  const [projects, setProjects] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [role, setRole] = useState("ADMIN"); // Default role for testing
  const [userEmail, setUserEmail] = useState("");

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decoded = jwtDecode(token);
        console.log("Decoded Token:", decoded);
        // setRole(decoded.roles); // Uncomment when integrating actual auth
        setUserEmail(decoded.name);
      } catch (error) {
        console.error("Invalid token", error);
      }
    }
  }, []);

  useEffect(() => {
    console.log(`${BASE_URL}`);
    fetch(`${BASE_URL}/projects/status/APPROVED`)
      .then((response) => response.json())
      .then((data) => {
        setProjects(data);
      })
      .catch((error) => console.error("Error fetching projects:", error));
  }, []);

  const trimmedSearchTerm = searchTerm.trim().toLowerCase();
  const filteredProjects = projects.filter((project) => {
    const title = project.title ? project.title.toLowerCase() : "";
    const description = project.description
      ? project.description.toLowerCase()
      : "";
    return (
      title.includes(trimmedSearchTerm) ||
      description.includes(trimmedSearchTerm)
    );
  });

  return (
    <div className="homepage-container">
      <Navbar role={role} />
      <div className="profile-container">
        <FaUserCircle className="profile-icon" size={32} />
        <p className="user-email">{userEmail}</p>
      </div>
      <h1>All Projects</h1>
      <input
        type="text"
        placeholder="Search projects..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className="search-bar"
      />
      <div className="project-list">
        {filteredProjects.length > 0 ? (
          filteredProjects.map((project) => (
            <div key={project.projectId} className="project-card">
              <h3>{project.title}</h3>
              <p>{project.description.split(" ").slice(0, 15).join(" ")}...</p>
              {/* <p>
                <strong>Goal:</strong> ${project.goalAmount}
              </p> */}
              <p>
                <strong>Deadline:</strong>{" "}
                {new Date(project.deadline).toLocaleDateString()}
              </p>

              {/* Pass role via Link state */}
              <Link to={`/project/${project.projectId}`} state={{ role }}>
                <button className="details-btn">View Details</button>
              </Link>
            </div>
          ))
        ) : (
          <p>No projects found.</p>
        )}
      </div>
    </div>
  );
};

export default HomePage;
