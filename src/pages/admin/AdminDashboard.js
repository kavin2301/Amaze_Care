import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { toast } from 'react-toastify';

const AdminDashboard = () => {
  const [counts, setCounts] = useState({
    users: 0,
    patients: 0,
    doctors: 0,
    appointments: 0,
  });

  const [loading, setLoading] = useState(true);
  const [lastUpdated, setLastUpdated] = useState(null);
  const token = localStorage.getItem("token");

  useEffect(() => {
    fetchCounts();
  }, []);

  const fetchCounts = async () => {
    try {
      setLoading(true);
      const headers = { Authorization: `Bearer ${token}` };

      const [usersRes, patientsRes, doctorsRes, appointmentsRes] = await Promise.all([
        axios.get(`${process.env.REACT_APP_API_BASE_URL}/admin/users`, { headers }),
        axios.get(`${process.env.REACT_APP_API_BASE_URL}/admin/patients`, { headers }),
        axios.get(`${process.env.REACT_APP_API_BASE_URL}/admin/doctors`, { headers }),
        axios.get(`${process.env.REACT_APP_API_BASE_URL}/admin/appointments`, { headers }),
      ]);

      setCounts({
        users: usersRes.data.length,
        patients: patientsRes.data.length,
        doctors: doctorsRes.data.length,
        appointments: appointmentsRes.data.length,
      });

      setLastUpdated(new Date().toLocaleString());
    } catch (err) {
      console.error("Failed to fetch metrics", err);
      toast.error("❌ Failed to load admin metrics");
    } finally {
      setLoading(false);
    }
  };

  const cards = [
    { label: "Users", count: counts.users, icon: "👥", color: "bg-primary" },
    { label: "Doctors", count: counts.doctors, icon: "👨‍⚕️👩‍⚕️", color: "bg-success" },
    { label: "Patients", count: counts.patients, icon: "🙋‍♀️🙋‍♂️", color: "bg-warning text-dark" },
    { label: "Appointments", count: counts.appointments, icon: "📅", color: "bg-info" },
  ];

  const tools = [
    { label: "👨‍⚕️👩‍⚕️ Manage Doctors", path: "/admin/manage-doctors" },
    { label: "🙋‍♀️🙋‍♂️ View Patients", path: "/admin/view-patients" },
    { label: "📅 View All Appointments", path: "/admin/view-appointments" },
    { label: "➕ Add New Admin", path: "/admin/create-admin" },
  ];

  if (loading) {
    return (
      <div className="container py-5 text-center">
        <div className="spinner-border text-primary mb-3" role="status" />
        <p>Loading dashboard...</p>
      </div>
    );
  }

  return (
    <div className="container py-5">
      <div className="text-center mb-4">
        <h2 className="fw-bold text-primary">🛡️ Admin Dashboard</h2>
        <p className="text-muted">Manage users, doctors, appointments, and system configurations.</p>
      </div>

    
      <div className="text-end text-muted small mb-3">
        Last updated: {lastUpdated}
      </div>

      <div className="row g-4 mb-5">
        {cards.map((card, index) => (
          <div className="col-lg-3 col-md-6 col-sm-12" key={index}>
            <div className={`card text-white shadow-sm ${card.color} hover-shadow`}>
              <div className="card-body text-center">
                <div className="display-5">{card.icon}</div>
                <h5 className="card-title mt-2">{card.label}</h5>
                <p className="card-text fs-4">{card.count}</p>
              </div>
            </div>
          </div>
        ))}
      </div>

 
      <div className="card shadow-sm p-4">
        <h4 className="mb-4 text-secondary">🛠️ Admin Tools</h4>
        <div className="row g-3">
          {tools.map((tool, i) => (
            <div className="col-md-6" key={i}>
              <Link to={tool.path} className="btn btn-outline-dark w-100 py-3 text-start">
                {tool.label}
              </Link>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
