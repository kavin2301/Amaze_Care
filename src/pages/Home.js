import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
  return (
    <div
      className="container d-flex flex-column align-items-center justify-content-center text-center"
      style={{ minHeight: '85vh' }}
    >
      <div className="card shadow-lg p-4 p-md-5 bg-white rounded-4" style={{ maxWidth: '600px', width: '100%' }}>
        <div className="mb-3">
          <img
            src="/logo192.png"
            alt="AmazeCare Logo"
            width={60}
            height={60}
            className="mb-2"
          />
          <h1 className="text-primary fw-bold">Welcome to AmazeCare 🏥</h1>
        </div>

        <p className="lead mt-2">
          A unified healthcare platform for <strong>Patients</strong>, <strong>Doctors</strong>, and <strong>Admins</strong>.
        </p>

        <hr />

        <p className="text-muted mb-3">
          Please log in to access your personalized dashboard.
        </p>

        <div className="d-flex justify-content-center gap-3 mt-3 flex-wrap">
          <Link to="/login" className="btn btn-primary px-4 py-2">
            🔐 Login
          </Link>
          <Link to="/register" className="btn btn-outline-secondary px-4 py-2">
            📝 Register
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Home;
