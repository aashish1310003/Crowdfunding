import { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import "../styles/styles.css";
import { BASE_URL } from "../api/api";
import axiosInstance from "../middleware/axiosInstance";

const UpdateProject = () => {
  const { projectId } = useParams();
  const navigate = useNavigate();
  const [project, setProject] = useState(null);

  useEffect(() => {
    axiosInstance
      .get(`${BASE_URL}/projects/${projectId}`)
      .then((res) => {
        const projectData = res.data;
        setProject({
          ...projectData,
          userId: localStorage.getItem("userId"), // Ensure userId is included
          deadline: projectData.deadline
            ? projectData.deadline.replace(" ", "T").slice(0, 16) // Convert to datetime-local format
            : "",
        });
      })
      .catch((err) => console.error("Error fetching project:", err));
  }, [projectId]);

  const handleChange = (e) => {
    setProject((prevProject) => ({
      ...prevProject,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    axiosInstance
      .post(`${BASE_URL}/projects/update`, project)
      .then(() => navigate("/my-projects"))
      .catch((err) => console.error("Error updating project:", err));
  };

  if (!project) return <p>Loading project details...</p>;

  return (
    <div className="create-container">
      <h2>Update Project</h2>
      <form onSubmit={handleSubmit} className="create-form">
        <input
          type="text"
          name="title"
          value={project.title || ""}
          placeholder="Project Title"
          onChange={handleChange}
          required
        />
        <textarea
          name="description"
          value={project.description || ""}
          placeholder="Project Description"
          onChange={handleChange}
          required></textarea>
        <input
          type="number"
          name="goalAmount"
          value={project.goalAmount || ""}
          placeholder="Goal Amount"
          onChange={handleChange}
          required
        />
        <input
          type="datetime-local"
          name="deadline"
          value={project.deadline || ""}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="reportPdfUrl"
          value={project.reportPdfUrl || ""}
          placeholder="Report PDF URL"
          onChange={handleChange}
          required
        />
        <button type="submit">Update Project</button>
      </form>
    </div>
  );
};

export default UpdateProject;
