import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { toast } from 'react-toastify';

const API = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';

const DoctorDashboard = () => {
  const [appointments, setAppointments] = useState([]);

  useEffect(() => {
    fetchAppointments();
  }, []);

  const fetchAppointments = async () => {
    try {
      const token = localStorage.getItem("token");
      const res = await axios.get(`${API}/appointments/doctor`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setAppointments(res.data);

      const today = new Date().toISOString().split("T")[0];
      const todaysConfirmed = res.data.filter(appt =>
        appt.appointmentDateTime.startsWith(today) && appt.status === "CONFIRMED"
      );

      if (todaysConfirmed.length > 0) {
        toast.info(`📌 You have ${todaysConfirmed.length} confirmed appointment(s) today`);
      }
    } catch (err) {
      toast.error("Failed to fetch appointments");
    }
  };

  const upcoming = appointments.find(
    appt => new Date(appt.appointmentDateTime) > new Date() && appt.status === "CONFIRMED"
  );

  const options = [
    {
      title: "View & Manage Appointments",
      icon: "📅",
      path: "/doctor/appointments",
      color: "bg-primary",
    },
    {
      title: "My Profile",
      icon: "👤",
      path: "/doctor/profile",
      color: "bg-secondary",
    },
  ];

  return (
    <div className="container py-5">
      <div className="text-center mb-4">
        <h2 className="fw-bold text-primary">🩺 Doctor Dashboard</h2>
        <p className="text-muted">Welcome, Doctor! Manage your appointments and profile easily.</p>
      </div>


      {upcoming && (
        <div className="alert alert-success d-flex justify-content-between align-items-center shadow-sm">
          <div>
            <strong>Upcoming:</strong> Appointment with{" "}
            <strong>{upcoming.patient?.user?.name || "a patient"}</strong>
          </div>
          <div>
            {new Date(upcoming.appointmentDateTime).toLocaleString()}
          </div>
        </div>
      )}

    
      <div className="row justify-content-center g-4 mt-4">
        {options.map((item, idx) => (
          <div key={idx} className="col-md-4 col-sm-6">
            <Link to={item.path} className="text-decoration-none">
              <div className={`card shadow-sm h-100 text-white ${item.color}`}>
                <div className="card-body d-flex flex-column align-items-center justify-content-center text-center">
                  <div className="display-4">{item.icon}</div>
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

export default DoctorDashboard;
