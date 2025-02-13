import React from "react";
import { Link } from "react-router-dom";
import "../styles/styles.css";

const Navbar = () => {
  return (
    <nav className="navbar">
      <h1 className="nav-title">Crowdfunding Portal</h1>
      <ul className="nav-links">
        <li>
          <Link to="/">All Projects</Link>
        </li>
        <li>
          <Link to="/create-project">Create Project</Link>
        </li>
        <li>
          <Link to="/my-projects">My Projects</Link>
        </li>
      </ul>
    </nav>
  );
};

export default Navbar;
