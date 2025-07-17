import React from "react";
import { Link } from "react-router-dom";

const PatientDashboard = () => {
  const options = [
    {
      path: "/patient/book-appointment",
      icon: "📅",
      title: "Book Appointment",
      color: "bg-primary",
    },
    {
      path: "/patient/appointments",
      icon: "📋",
      title: "My Appointments",
      color: "bg-success",
    },
    {
      path: "/patient/prescriptions",
      icon: "💊",
      title: "My Prescriptions",
      color: "bg-info",
    },
    {
      path: "/patient/tests",
      icon: "🧪",
      title: "Test Recommendations",
      color: "bg-warning text-dark",
    },
    {
      path: "/patient/profile",
      icon: "👤",
      title: "My Profile",
      color: "bg-secondary",
    },
  ];

  return (
    <div className="container py-5">
      <div className="text-center mb-4">
        <h2 className="fw-bold text-success">👨‍⚕️ Patient Dashboard</h2>
        <p className="text-muted">Manage your appointments, prescriptions, and profile.</p>
      </div>

      <div className="row g-4">
        {options.map((item, idx) => (
          <div key={idx} className="col-md-4 col-sm-6">
            <Link to={item.path} className="text-decoration-none">
              <div
                className={`card shadow-sm h-100 text-white ${item.color}`}
                style={{
                  borderRadius: "1rem",
                  transition: "transform 0.2s",
                }}
                onMouseEnter={(e) => e.currentTarget.style.transform = "scale(1.03)"}
                onMouseLeave={(e) => e.currentTarget.style.transform = "scale(1)"}
              >
                <div className="card-body d-flex flex-column align-items-center justify-content-center text-center py-4">
                  <div className="fs-1">{item.icon}</div>
                  <h5 className="mt-3">{item.title}</h5>
                </div>
              </div>
            </Link>
          </div>
        ))}
      </div>
    </div>
  );
};

export default PatientDashboard;
