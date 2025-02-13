import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "../styles/styles.css";

const CreateProject = () => {
  const [project, setProject] = useState({
    title: "",
    description: "",
    goalAmount: "",
    deadline: "",
    reportPdfUrl: "",
    status: "PENDING",
    userId: 1,
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setProject({ ...project, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    axios
      .post("http://localhost:5000/projects", project)
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
          required
        ></textarea>
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
