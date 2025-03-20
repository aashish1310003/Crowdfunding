import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { BASE_URL } from "../api/api";

const AdminProjectDetails = () => {
  const { projectId } = useParams();
  const navigate = useNavigate();
  const [project, setProject] = useState(null);
  const [creator, setCreator] = useState(null);
  const [collectedAmount, setCollectedAmount] = useState(0);

  useEffect(() => {
    fetch(`${BASE_URL}/projects/${projectId}`)
      .then((response) => response.json())
      .then((data) => {
        setProject(data);
        return fetch(`${BASE_URL}/users/by/id?id=${data.userId}`);
      })
      .then((response) => response.json())
      .then((userData) => setCreator(userData["data"]))
      .catch((error) => console.error("Error fetching data:", error));
  }, [projectId]);

  useEffect(() => {
    fetch(`${BASE_URL}/donations/donation/sum/${projectId}/project`)
      .then((response) => response.json())
      .then((data) => setCollectedAmount(data.amount))
      .catch((error) =>
        console.error("Error fetching collected amount:", error)
      );
  }, [projectId]);

  const handleApproval = (status) => {
    fetch(`${BASE_URL}/projects/${projectId}/status`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ status }),
    })
      .then((response) => response.json())
      .then(() => {
        alert(`Project ${status}`);
        navigate("/admin/projects");
      })
      .catch((error) => console.error("Error updating project status:", error));
  };

  if (!project || !creator) {
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
        <strong>Collected:</strong> ₹{collectedAmount}
      </p>
      <p>
        <strong>Deadline:</strong>{" "}
        {new Date(project.deadline).toLocaleDateString()}
      </p>
      <p>
        <strong>Status:</strong> {project.status}
      </p>
      <p>
        <strong>Creator:</strong> {creator.name} ({creator.email})
      </p>

      <div className="pdf-viewer">
        <iframe
          src={
            project.reportPdfUrl.includes("drive.google.com")
              ? project.reportPdfUrl.replace("/view", "/preview")
              : project.reportPdfUrl
          }
          width="100%"
          height="500px"
          title="Project PDF"></iframe>
      </div>

      <button
        className="approve-button"
        onClick={() => handleApproval("APPROVED")}>
        Approve
      </button>
      <button
        className="reject-button"
        onClick={() => handleApproval("REJECTED")}>
        Reject
      </button>
    </div>
  );
};

export default AdminProjectDetails;
