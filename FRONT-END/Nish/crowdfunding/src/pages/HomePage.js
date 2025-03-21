import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { FaUserCircle } from "react-icons/fa"; // Import profile icon
import Navbar from "../components/Navbar";
import "../styles/styles.css";
import axiosInstance from "../middleware/axiosInstance";

const HomePage = () => {
  const [projects, setProjects] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [role, setRole] = useState(null);
  const [userEmail, setUserEmail] = useState("");
  const [tokenLoaded, setTokenLoaded] = useState(false); // New state

  // ✅ Load token and set user info
  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decoded = jwtDecode(token);
        console.log("Decoded Token:", decoded);
        setRole(decoded.roles); // Ensure this matches your backend response
        setUserEmail(decoded.name);
      } catch (error) {
        console.error("Invalid token", error);
      }
    }
    setTokenLoaded(true); // Mark that token is now processed
  }, []);

  // ✅ Fetch projects only after token is loaded
  useEffect(() => {
    if (!tokenLoaded) return; // Ensure token is set before API call

    axiosInstance
      .get("/projects/status/APPROVED")
      .then(({ data }) => setProjects(data))
      .catch((error) => console.error("Error fetching projects:", error));
  }, [tokenLoaded]); // Only runs when tokenLoaded is true

  // ✅ Filter projects based on search input
  const trimmedSearchTerm = searchTerm.trim().toLowerCase();
  const filteredProjects = projects.filter((project) =>
    [project.title, project.description]
      .filter(Boolean)
      .some((text) => text.toLowerCase().includes(trimmedSearchTerm))
  );

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
              <p>
                <strong>Deadline:</strong>{" "}
                {new Date(project.deadline).toLocaleDateString()}
              </p>
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
