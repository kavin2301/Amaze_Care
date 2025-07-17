import React from "react";
import { Button } from "react-bootstrap";

const StatusToggleButton = ({ status, onToggle }) => {
  const isActive = status === "ACTIVE";

  return (
    <Button
      size="sm"
      variant={isActive ? "success" : "secondary"}
      onClick={() => onToggle(isActive ? "INACTIVE" : "ACTIVE")}
    >
      {isActive ? "Active 🔓" : "Inactive 🔒"}
    </Button>
  );
};

export default StatusToggleButton;