import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";

const DonationPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [project, setProject] = useState(null);
  const [amount, setAmount] = useState("");

  useEffect(() => {
    fetch(`http://localhost:8080/projects/${id}`)
      .then((response) => response.json())
      .then((data) => setProject(data))
      .catch((error) =>
        console.error("Error fetching project details:", error)
      );
  }, [id]);

  const handlePayment = async () => {
    if (!amount || amount <= 0) {
      alert("Please enter a valid amount.");
      return;
    }

    try {
      // Store payment details in localStorage before redirecting
      localStorage.setItem("currentProjectId", id);
      localStorage.setItem("donationAmount", amount);

      const response = await fetch(
        "http://localhost:8080/payment/stripe/checkout",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            name: project.title,
            quantity: 1,
            amount: amount * 100, // Convert INR to paise
            currency: "INR",
          }),
        }
      );

      const data = await response.json();

      if (data.status === "SUCCESS") {
        window.location.href = data.sessionUrl; // Redirect to Stripe checkout
      } else {
        console.error("Error creating payment session:", data.message);
      }
    } catch (error) {
      console.error("Payment error:", error);
    }
  };

  if (!project) {
    return <p>Loading...</p>;
  }

  return (
    <div className="donation-page">
      <h2>Donate to {project.title}</h2>
      <p>{project.description}</p>
      <p>
        <strong>Goal:</strong> ₹{project.goalAmount}
      </p>
      <label>
        Enter Amount (INR):
        <input
          type="number"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          min="1"
        />
      </label>
      <button onClick={handlePayment}>Proceed to Payment</button>
      <button onClick={() => navigate(-1)}>Back</button>
    </div>
  );
};

export default DonationPage;
