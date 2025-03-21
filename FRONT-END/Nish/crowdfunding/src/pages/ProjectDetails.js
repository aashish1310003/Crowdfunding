import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import axiosInstance from "../middleware/axiosInstance";
import { CircularProgress } from "@mui/material";
const ProjectDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [project, setProject] = useState(null);
  const [userId, setUserId] = useState(null);
  const [creator, setCreator] = useState(null);
  const [collectedAmount, setCollectedAmount] = useState(0);

  useEffect(() => {
    // Retrieve user ID from JWT token
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
    // Fetch project, creator, and collected amount concurrently using axios
    const fetchProjectDetails = axiosInstance.get(`/projects/${id}`);
    const fetchCollectedAmount = axiosInstance
      .get(`/donations/donation/sum/${id}/project`)
      .catch(() => ({ data: { amount: 0 } })); // Default to 0 if API fails

    Promise.all([fetchProjectDetails, fetchCollectedAmount])
      .then(([projectRes, collectedRes]) => {
        setProject(projectRes.data);
        setCollectedAmount(collectedRes.data.amount || 0);
        return axiosInstance.get(`/users/by/id?id=${projectRes.data.userId}`);
      })
      .then((userRes) => setCreator(userRes.data.data))
      .catch((error) => console.error("Error fetching data:", error));
  }, [id]);

  if (!project || !creator) {
    // return <p>Loading project details...</p>;
    return (
      <p>
        <CircularProgress />{" "}
      </p>
    );
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

      {project.status === "APPROVED" && (
        <button
          className="donate-button"
          onClick={() => navigate(`/payment/${id}`)}>
          Donate
        </button>
      )}

      {userId && project.userId === userId && project.status !== "APPROVED" && (
        <button
          className="update-button"
          onClick={() => navigate(`/update/${id}`)}>
          Update
        </button>
      )}
    </div>
  );
};

export default ProjectDetails;
