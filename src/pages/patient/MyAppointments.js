
import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const MyAppointments = () => {
  const [appointments, setAppointments] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchAppointments();
  }, []);

  const fetchAppointments = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(
        `${process.env.REACT_APP_API_BASE_URL}/appointments/patient`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const sorted = response.data.sort(
        (a, b) =>
          new Date(b.appointmentDateTime) - new Date(a.appointmentDateTime)
      );

      setAppointments(sorted);
    } catch (error) {
      console.error("Failed to fetch appointments", error);
      toast.error("Unable to load appointments.");
    }
  };

  const cancelAppointment = async (id) => {
    try {
      const token = localStorage.getItem("token");
      await axios.delete(
        `${process.env.REACT_APP_API_BASE_URL}/appointments/${id}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      toast.success("Appointment cancelled.");
      fetchAppointments();
    } catch (error) {
      console.error("Cancel error:", error);
      toast.error("Failed to cancel appointment.");
    }
  };

  const getStatusClass = (status) => {
    switch (status) {
      case "CONFIRMED":
        return "bg-success";
      case "REJECTED":
        return "bg-danger";
      case "CANCELLED":
        return "bg-dark";
      case "COMPLETED":
        return "bg-primary";
      default:
        return "bg-secondary";
    }
  };

  return (
    <div className="container mt-5">
      <h2 className="mb-2 text-primary">📅 My Appointments</h2>
      <p className="text-muted">Total Appointments: {appointments.length}</p>

      <button
        className="btn btn-outline-secondary mb-4"
        onClick={() => navigate("/patient/dashboard")}
      >
        ← Back to Dashboard
      </button>

      {appointments.length === 0 ? (
        <div className="alert alert-info">No appointments found.</div>
      ) : (
        <div className="table-responsive">
          <table className="table table-hover table-bordered shadow-sm text-nowrap">
            <thead className="table-primary text-center">
              <tr>
                <th>Date & Time</th>
                <th>Doctor</th>
                <th>Status</th>
                <th>Notes</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody className="text-center align-middle">
              {appointments.map((appt) => (
                <tr key={appt.id}>
                  <td>{new Date(appt.appointmentDateTime).toLocaleString()}</td>
                  <td>
                    {appt.doctor?.user?.name || "N/A"}
                    <br />
                    <small className="text-muted">
                      {appt.doctor?.user?.email}
                    </small>
                  </td>
                  <td>
                    <span
                      className={`badge px-2 py-1 ${getStatusClass(
                        appt.status
                      )}`}
                    >
                      {appt.status}
                    </span>
                  </td>
                  <td>{appt.notes || "-"}</td>
                  <td>
                    {appt.status === "REQUESTED" ? (
                      <button
                        className="btn btn-sm btn-outline-danger"
                        onClick={() => cancelAppointment(appt.id)}
                      >
                        Cancel
                      </button>
                    ) : (
                      <span className="badge bg-light text-muted">N/A</span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default MyAppointments;
