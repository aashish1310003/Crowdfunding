import React from "react";
import { Link } from "react-router-dom";
import { Home, PlusCircle, FolderOpen, Coins } from "lucide-react";
import "../styles/Navbar.css";

const Navbar = ({ role }) => {
  return (
    <nav className="navbar">
      <Link to="/home" className="nav-brand">
        <Coins className="nav-logo" />
        <h1 className="nav-title">CrowdFund Hub</h1>
      </Link>

      <ul className="nav-links">
        <li>
          <Link to="/home">
            <Home className="nav-icon" />
            <span>All Projects</span>
          </Link>
        </li>
        {role === "STUDENT" && (
          <>
            <li>
              <Link to="/create-project">
                <PlusCircle className="nav-icon" />
                <span>Create Project</span>
              </Link>
            </li>
            <li>
              <Link to="/my-projects">
                <FolderOpen className="nav-icon" />
                <span>My Projects</span>
              </Link>
            </li>
          </>
        )}
        {role === "DONOR" && (
          <li>
            <Link to="/my-projects">
              <FolderOpen className="nav-icon" />
              <span>My Projects</span>
            </Link>
          </li>
        )}
      </ul>
    </nav>
  );
};

export default Navbar;
