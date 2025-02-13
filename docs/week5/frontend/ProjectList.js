import { useEffect, useState } from "react";
import axios from "axios";
import "../styles/styles.css";

const ProjectList = () => {
  const [projects, setProjects] = useState([]);

  useEffect(() => {
    axios
      .get("http://localhost:5000/projects")
      .then((res) => setProjects(res.data))
      .catch((err) => console.error(err));
  }, []);

  return (
    <div className="project-list">
      <h2>All Projects</h2>
      <div className="projects-container">
        {projects.map((project) => (
          <div key={project.projectId} className="project-card">
            <h3>{project.title}</h3>
            <p>
              <strong>Description:</strong> {project.description}
            </p>
            <p>
              <strong>Goal Amount:</strong> $
              {project.goalAmount.toLocaleString()}
            </p>
            <p>
              <strong>Deadline:</strong>{" "}
              {new Date(project.deadline).toLocaleString()}
            </p>
            <p>
              <strong>Status:</strong>{" "}
              <span className={`status ${project.status.toLowerCase()}`}>
                {project.status}
              </span>
            </p>
            <a
              href={project.reportPdfUrl}
              target="_blank"
              rel="noopener noreferrer"
              className="report-link"
            >
              View Report
            </a>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ProjectList;
