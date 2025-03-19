import React, { useEffect, useState, useRef } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { BASE_URL } from "../api/api";

const PaymentSuccess = () => {
  const [searchParams] = useSearchParams();
  const sessionId = searchParams.get("session_id");
  const amount = searchParams.get("amount");
  const projectId = localStorage.getItem("currentProjectId");
  const userId = localStorage.getItem("userId");
  const navigate = useNavigate();

  const [isSaved, setIsSaved] = useState(false);
  const hasSaved = useRef(false); // ✅ Prevent duplicate calls

  useEffect(() => {
    const saveDonation = async () => {
      if (!sessionId || isSaved || hasSaved.current) return; // Prevent duplicate calls
      hasSaved.current = true; // Mark as executed

      try {
        const response = await fetch(`${BASE_URL}/donations/addDonation`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            donationId: sessionId.toString(),
            amount: amount / 100,
            donorVisibility: "PUBLIC",
            status: "COMPLETED",
            userId: userId,
            projectId: projectId,
          }),
        });

        if (response.ok) {
          console.log("Donation saved successfully.");
          setIsSaved(true);
        } else {
          console.error("Error saving donation.");
        }
      } catch (error) {
        console.error("Error:", error);
      }
    };

    saveDonation();
  }, [sessionId, amount, userId, projectId, isSaved]);

  return (
    <div className="success-page">
      <h2>Payment Successful 🎉</h2>
      <p>Thank you for your donation of ₹{amount}.</p>
      <button onClick={() => navigate("/home")}>Back to Home</button>
    </div>
  );
};

export default PaymentSuccess;
