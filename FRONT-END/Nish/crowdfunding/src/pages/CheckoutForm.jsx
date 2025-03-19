import React, { useState } from "react";
import { CardElement, useStripe, useElements } from "@stripe/react-stripe-js";

const CheckoutForm = ({ projectId }) => {
  const stripe = useStripe();
  const elements = useElements();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);

    if (!stripe || !elements) return;

    const { error, paymentIntent } = await stripe.confirmCardPayment(
      "your-secret-stripe-key-here",
      {
        payment_method: {
          card: elements.getElement(CardElement),
        },
      }
    );

    if (error) {
      setError(error.message);
      setLoading(false);
    } else {
      console.log("Payment successful:", paymentIntent);
      alert("Payment successful!");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="checkout-form">
      <CardElement />
      <button type="submit" disabled={!stripe || loading}>
        {loading ? "Processing..." : "Pay Now"}
      </button>
      {error && <p className="error">{error}</p>}
    </form>
  );
};

export default CheckoutForm;
