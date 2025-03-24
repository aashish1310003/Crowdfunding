import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { UserCircle, Search, Calendar, ArrowRight, Loader } from "lucide-react";
import Navbar from "../components/Navbar";
import "../styles/HomePage.css";
import axiosInstance from "../middleware/axiosInstance";

const HomePage = () => {
  const [projects, setProjects] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [role, setRole] = useState(null);
  const [userEmail, setUserEmail] = useState("");
  const [tokenLoaded, setTokenLoaded] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decoded = jwtDecode(token);
        setRole(decoded.roles);
        setUserEmail(decoded.name);
      } catch (error) {
        console.error("Invalid token", error);
      }
    }
    setTokenLoaded(true);
  }, []);

  useEffect(() => {
    if (!tokenLoaded) return;

    setLoading(true);
    axiosInstance
      .get("/projects/status/APPROVED")
      .then(({ data }) => {
        setProjects(data);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error fetching projects:", error);
        setLoading(false);
      });
  }, [tokenLoaded]);

  const trimmedSearchTerm = searchTerm.trim().toLowerCase();
  const filteredProjects = projects.filter((project) =>
    [project.title, project.description]
      .filter(Boolean)
      .some((text) => text.toLowerCase().includes(trimmedSearchTerm))
  );

  return (
    <div className="homepage-container">
      <Navbar role={role} />

      <div className="content-wrapper">
        <div className="profile-container">
          <UserCircle className="profile-icon" />
          <span className="user-email">{userEmail}</span>
        </div>

        <div className="search-container">
          <h1 className="page-title">Discover Projects</h1>
          <div className="search-wrapper">
            <Search className="search-icon" />
            <input
              type="text"
              placeholder="Search projects by title or description..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="search-bar"
            />
          </div>
        </div>

        {loading ? (
          <div className="loading-state">
            <Loader className="spinner" />
            <p>Loading projects...</p>
          </div>
        ) : (
          <div className="project-list">
            {filteredProjects.length > 0 ? (
              filteredProjects.map((project) => (
                <div key={project.projectId} className="project-card">
                  <div className="card-content">
                    <h3 className="project-title">{project.title}</h3>
                    <p className="project-description">
                      {project.description.split(" ").slice(0, 15).join(" ")}...
                    </p>
                    <div className="project-meta">
                      <div className="deadline">
                        <Calendar className="meta-icon" />
                        <span>
                          {new Date(project.deadline).toLocaleDateString()}
                        </span>
                      </div>
                    </div>
                    <Link
                      to={`/project/${project.projectId}`}
                      state={{ role }}
                      className="view-details"
                    >
                      View Details
                      <ArrowRight className="arrow-icon" />
                    </Link>
                  </div>
                </div>
              ))
            ) : (
              <div className="no-results">
                <p>No projects found matching your search.</p>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default HomePage;
