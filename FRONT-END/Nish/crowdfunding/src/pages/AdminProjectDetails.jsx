import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { BASE_URL } from "../api/api";
import axiosInstance from "../middleware/axiosInstance";

const AdminProjectDetails = () => {
  const { projectId } = useParams();
  const navigate = useNavigate();
  const [project, setProject] = useState(null);
  const [creator, setCreator] = useState(null);
  const [collectedAmount, setCollectedAmount] = useState(0);

  useEffect(() => {
    axiosInstance
      .get(`/projects/${projectId}`)
      .then(({ data }) => {
        setProject(data);
        return axiosInstance.get(`/users/by/id`, {
          params: { id: data.userId },
        });
      })
      .then(({ data }) => setCreator(data["data"]))
      .catch((error) => console.error("Error fetching data:", error));
  }, [projectId]);

  useEffect(() => {
    axiosInstance
      .get(`/donations/donation/sum/${projectId}/project`)
      .then(({ data }) => setCollectedAmount(data.amount))
      .catch((error) =>
        console.error("Error fetching collected amount:", error)
      );
  }, [projectId]);

  const handleApproval = (status) => {
    axiosInstance
      .put(`/projects/${projectId}/status`, { status })
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
