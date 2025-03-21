import React, { useState } from "react";

const ExpandableToast = ({ message, details }) => {
  const [expanded, setExpanded] = useState(false);

  return (
    <div>
      <p style={{ margin: 0, fontWeight: "bold" }}>{message}</p>
      {expanded && (
        <p style={{ marginTop: 5, fontSize: "0.9em", whiteSpace: "pre-line" }}>
          {details}
        </p>
      )}
      {details && (
        <button
          onClick={() => setExpanded(!expanded)}
          style={{
            background: "none",
            border: "none",
            color: "blue",
            cursor: "pointer",
            padding: 0,
            marginTop: 5,
          }}>
          {expanded ? "▲ Show Less" : "▼ Show More"}
        </button>
      )}
    </div>
  );
};

export default ExpandableToast;
