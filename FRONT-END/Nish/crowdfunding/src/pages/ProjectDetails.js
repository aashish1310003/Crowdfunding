import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { BASE_URL } from "../api/api";

const ProjectDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [project, setProject] = useState(null);

  useEffect(() => {
    fetch(`${BASE_URL}/projects/${id}`)
      .then((response) => response.json())
      .then((data) => setProject(data))
      .catch((error) =>
        console.error("Error fetching project details:", error)
      );
  }, [id]);

  if (!project) {
    return <p>Loading project details...</p>;
  }

  return (
    <div className="project-details">
      <h2>{project.title}</h2>
      <p>{project.description}</p>
      <p>
        <strong>Goal:</strong> ₹{project.goalAmount}
      </p>
      <p>
        <strong>Deadline:</strong>{" "}
        {new Date(project.deadline).toLocaleDateString()}
      </p>
      <p>
        <strong>Status:</strong> {project.status}
      </p>
      <p>
        <strong>Creator:</strong> {project.user?.name} ({project.user?.email})
      </p>

      <div className="pdf-viewer">
        <iframe
          src={project.reportPdfUrl}
          width="100%"
          height="500px"
          title="Project PDF"></iframe>
      </div>

      <button
        className="donate-button"
        onClick={() => navigate(`/payment/${id}`)}>
        Donate
      </button>
    </div>
  );
};

export default ProjectDetails;
