import React from "react";
import { useNavigate } from "react-router-dom";

const PaymentFailure = () => {
  localStorage.removeItem("currentProjectId"); // Remove after processing
  const navigate = useNavigate();

  return (
    <div className="failure-page">
      <h2>Payment Failed ❌</h2>
      <p>Something went wrong. Please try again.</p>
      <button onClick={() => navigate("/home")}>Back to Home</button>
    </div>
  );
};

export default PaymentFailure;
