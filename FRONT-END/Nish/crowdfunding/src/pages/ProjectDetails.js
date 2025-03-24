import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import axiosInstance from "../middleware/axiosInstance";
import { CircularProgress } from "@mui/material";
import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from "recharts";
import {
  Calendar,
  Target,
  User,
  FileText,
  AlertCircle,
  DollarSign,
  Clock,
  Award,
} from "lucide-react";
import "../styles/ProjectDetails.css";

const COLORS = ["#6366f1", "#34d399"]; // Indigo and Emerald

const RADIAN = Math.PI / 180;
const renderCustomizedLabel = ({
  cx,
  cy,
  midAngle,
  innerRadius,
  outerRadius,
  percent,
}) => {
  const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
  const x = cx + radius * Math.cos(-midAngle * RADIAN);
  const y = cy + radius * Math.sin(-midAngle * RADIAN);

  return (
    <text
      x={x}
      y={y}
      fill="white"
      textAnchor={x > cx ? "start" : "end"}
      dominantBaseline="central"
      className="chart-label"
    >
      {`${(percent * 100).toFixed(0)}%`}
    </text>
  );
};

const CustomTooltip = ({ active, payload }) => {
  if (active && payload && payload.length) {
    return (
      <div className="custom-tooltip">
        <p>{`${payload[0].name}: ₹${payload[0].value.toLocaleString()}`}</p>
      </div>
    );
  }
  return null;
};

const ProjectDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [project, setProject] = useState(null);
  const [userId, setUserId] = useState(null);
  const [creator, setCreator] = useState(null);
  const [collectedAmount, setCollectedAmount] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
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
    const fetchData = async () => {
      try {
        const [projectRes, collectedRes] = await Promise.all([
          axiosInstance.get(`/projects/${id}`),
          axiosInstance
            .get(`/donations/donation/sum/${id}/project`)
            .catch(() => ({ data: { amount: 0 } })),
        ]);

        setProject(projectRes.data);
        setCollectedAmount(collectedRes.data.amount || 0);

        const userRes = await axiosInstance.get(
          `/users/by/id?id=${projectRes.data.userId}`
        );
        setCreator(userRes.data.data);
      } catch (error) {
        console.error("Error fetching data:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id]);

  if (loading) {
    return (
      <div className="loading-container">
        <CircularProgress className="loading-spinner" />
      </div>
    );
  }

  if (!project || !creator) {
    return (
      <div className="error-container">
        <AlertCircle className="error-icon" />
        <p>Project not found</p>
      </div>
    );
  }

  const chartData = [
    { name: "Collected Amount", value: collectedAmount },
    {
      name: "Remaining Amount",
      value: Math.max(0, project.goalAmount - collectedAmount),
    },
  ];

  const progress = (collectedAmount / project.goalAmount) * 100;
  const daysLeft = Math.ceil(
    (new Date(project.deadline) - new Date()) / (1000 * 60 * 60 * 24)
  );

  return (
    <div className="project-details-container">
      <div className="project-card">
        <div className="project-header">
          <h1>{project.title}</h1>
          <div className="status-badge">{project.status}</div>
        </div>

        <div className="project-grid">
          <div className="project-info">
            <div className="info-item">
              <Target className="icon target" />
              <span>Goal: ₹{project.goalAmount.toLocaleString()}</span>
            </div>
            <div className="info-item">
              <DollarSign className="icon money" />
              <span>Collected: ₹{collectedAmount.toLocaleString()}</span>
            </div>
            <div className="info-item">
              <Clock className="icon time" />
              <span>{daysLeft} days left</span>
            </div>
            <div className="info-item">
              <Calendar className="icon calendar" />
              <span>
                Deadline: {new Date(project.deadline).toLocaleDateString()}
              </span>
            </div>
            <div className="info-item">
              <User className="icon user" />
              <span>Creator: {creator.name}</span>
            </div>
            <div className="info-item">
              <Award className="icon award" />
              <span>Progress: {progress.toFixed(1)}%</span>
            </div>
          </div>

          <div className="chart-section">
            <div className="progress-overlay">
              <span className="progress-text">{progress.toFixed(0)}%</span>
              <span className="progress-label">Funded</span>
            </div>
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={chartData}
                  dataKey="value"
                  nameKey="name"
                  cx="50%"
                  cy="50%"
                  innerRadius={60}
                  outerRadius={80}
                  labelLine={false}
                  label={renderCustomizedLabel}
                >
                  {chartData.map((entry, index) => (
                    <Cell
                      key={`cell-${index}`}
                      fill={COLORS[index]}
                      className="pie-cell"
                    />
                  ))}
                </Pie>
                <Tooltip content={<CustomTooltip />} />
                <Legend verticalAlign="bottom" height={36} iconType="circle" />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="project-description">
          <h2>
            <FileText className="icon description" />
            Project Description
          </h2>
          <p>{project.description}</p>
        </div>

        <div className="document-viewer">
          <h3>Project Document</h3>
          <div className="pdf-container">
            <iframe
              src={
                project.reportPdfUrl.includes("drive.google.com")
                  ? project.reportPdfUrl.replace("/view", "/preview")
                  : project.reportPdfUrl
              }
              title="Project PDF"
            />
          </div>
        </div>

        <div className="action-buttons">
          {project.status === "APPROVED" && (
            <button
              className="donate-button"
              onClick={() => navigate(`/payment/${id}`)}
            >
              <DollarSign className="button-icon" />
              Donate Now
            </button>
          )}

          {userId &&
            project.userId === userId &&
            project.status !== "APPROVED" && (
              <button
                className="update-button"
                onClick={() => navigate(`/update/${id}`)}
              >
                Update Project
              </button>
            )}
        </div>
      </div>
    </div>
  );
};

export default ProjectDetails;
