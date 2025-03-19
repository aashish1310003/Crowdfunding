import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "../styles/styles.css";
import { BASE_URL } from "../api/api";

const CreateProject = () => {
  const [project, setProject] = useState({
    title: "",
    description: "",
    goalAmount: 0,
    deadline: "",
    reportPdfUrl: "",
    userId: Number(localStorage.getItem("userId")) || null,
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    let value = e.target.value;
    if (e.target.name === "goalAmount") {
      value = parseFloat(value) || 0; // Ensure it's a number
    }
    setProject({ ...project, [e.target.name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    axios
      .post(`${BASE_URL}/projects/create`, project) // Corrected URL
      .then(() => navigate("/"))
      .catch((err) => console.error(err));
  };

  return (
    <div className="create-container">
      <h2>Create a New Project</h2>
      <form onSubmit={handleSubmit} className="create-form">
        <input
          type="text"
          name="title"
          placeholder="Project Title"
          onChange={handleChange}
          required
        />
        <textarea
          name="description"
          placeholder="Project Description"
          onChange={handleChange}
          required></textarea>
        <input
          type="number"
          name="goalAmount"
          placeholder="Goal Amount"
          onChange={handleChange}
          required
        />
        <input
          type="datetime-local"
          name="deadline"
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="reportPdfUrl"
          placeholder="Report PDF URL"
          onChange={handleChange}
          required
        />
        <button type="submit">Create Project</button>
      </form>
    </div>
  );
};

export default CreateProject;
