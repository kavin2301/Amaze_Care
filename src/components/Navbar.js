import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const Navbar = () => {
  const { auth, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const getDashboardPath = () => {
    switch (auth?.role) {
      case "ADMIN":
        return "/admin/dashboard";
      case "DOCTOR":
        return "/doctor/dashboard";
      case "PATIENT":
        return "/patient/dashboard";
      default:
        return "/";
    }
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm px-4 py-2">
      <div className="container-fluid">
        <Link className="navbar-brand fw-bold fs-4" to="/">
          🩺 AmazeCare
        </Link>

        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarContent"
          aria-controls="navbarContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse justify-content-end" id="navbarContent">
          <ul className="navbar-nav align-items-center gap-2">
            {auth?.token && (
              <>
                <li className="nav-item">
                  <Link to={getDashboardPath()} className="btn btn-light btn-sm">
                    🏠 Dashboard
                  </Link>
                </li>
                <li className="nav-item d-none d-md-inline text-white small">
                  Logged in as: <strong>{auth.role}</strong>
                </li>
                <li className="nav-item">
                  <button onClick={handleLogout} className="btn btn-outline-light btn-sm">
                    🚪 Logout
                  </button>
                </li>
              </>
            )}

            {!auth?.token && (
              <>
                <li className="nav-item">
                  <Link to="/login" className="btn btn-outline-light btn-sm">
                    🔐 Login
                  </Link>
                </li>
                <li className="nav-item">
                  <Link to="/register" className="btn btn-light btn-sm">
                    📝 Register
                  </Link>
                </li>
              </>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
