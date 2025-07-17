import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import { Link, useNavigate } from "react-router-dom";
import UpdateHistoryModal from "./components/UpdateHistoryModal";

const DoctorAppointments = () => {
  const [appointments, setAppointments] = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [updatingId, setUpdatingId] = useState(null);
  const [showHistoryModal, setShowHistoryModal] = useState(false);
  const [selectedAppointment, setSelectedAppointment] = useState(null);
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [sortOrder, setSortOrder] = useState("DESC");
  const [searchName, setSearchName] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    fetchAppointments();
  }, []);

  useEffect(() => {
    applyFilters();
  }, [appointments, statusFilter, sortOrder, searchName]);

  const fetchAppointments = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(
        `${process.env.REACT_APP_API_BASE_URL}/appointments/doctor`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setAppointments(response.data);
    } catch (error) {
      toast.error("Failed to fetch appointments");
    }
  };

  const applyFilters = () => {
    let result = [...appointments];

    if (statusFilter !== "ALL") {
      result = result.filter((a) => a.status === statusFilter);
    }

    if (searchName.trim()) {
      result = result.filter((a) =>
        a.patient?.user?.name?.toLowerCase().includes(searchName.toLowerCase())
      );
    }

    result.sort((a, b) =>
      sortOrder === "DESC"
        ? new Date(b.appointmentDateTime) - new Date(a.appointmentDateTime)
        : new Date(a.appointmentDateTime) - new Date(b.appointmentDateTime)
    );

    setFiltered(result);
  };

  const updateStatus = async (id, status) => {
    setUpdatingId(id);
    try {
      const token = localStorage.getItem("token");
      await axios.put(
        `${process.env.REACT_APP_API_BASE_URL}/appointments/${id}/status`,
        null,
        {
          params: { status },
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      toast.success(`Appointment ${status.toLowerCase()}!`);
      fetchAppointments();
    } catch (err) {
      toast.error("Failed to update status");
    } finally {
      setUpdatingId(null);
    }
  };

  const handleOpenHistoryModal = (appt) => {
    setSelectedAppointment(appt);
    setShowHistoryModal(true);
  };

  const handleSaveHistory = async (newHistory) => {
    try {
      const token = localStorage.getItem("token");
      await axios.put(
        `${process.env.REACT_APP_API_BASE_URL}/doctor/patient/${selectedAppointment.patient.id}/medical-history`,
        newHistory,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
      toast.success("Medical history updated");
      setShowHistoryModal(false);
      setSelectedAppointment(null);
    } catch (err) {
      toast.error("Failed to update medical history");
    }
  };

  return (
    <div className="container mt-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h3 className="text-primary">🗓️ My Appointments</h3>
        <button
          className="btn btn-outline-secondary"
          onClick={() => navigate("/doctor/dashboard")}
        >
          ← Back to Dashboard
        </button>
      </div>

     
      <div className="card shadow-sm p-3 mb-4">
        <div className="row g-3">
          <div className="col-md-4">
            <label className="form-label">Filter by Status</label>
            <select
              className="form-select"
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
            >
              <option value="ALL">All</option>
              <option value="REQUESTED">Requested</option>
              <option value="CONFIRMED">Confirmed</option>
              <option value="COMPLETED">Completed</option>
              <option value="REJECTED">Rejected</option>
              <option value="CANCELLED">Cancelled</option>
            </select>
          </div>

          <div className="col-md-4">
            <label className="form-label">Search by Patient Name</label>
            <input
              type="text"
              className="form-control"
              placeholder="Enter patient name"
              value={searchName}
              onChange={(e) => setSearchName(e.target.value)}
            />
          </div>

          <div className="col-md-4">
            <label className="form-label">Sort by Date & Time</label>
            <select
              className="form-select"
              value={sortOrder}
              onChange={(e) => setSortOrder(e.target.value)}
            >
              <option value="DESC">Newest First</option>
              <option value="ASC">Oldest First</option>
            </select>
          </div>
        </div>
      </div>

      {filtered.length === 0 ? (
        <div className="alert alert-info">No appointments found.</div>
      ) : (
        <div className="table-responsive">
          <table className="table table-hover table-bordered align-middle">
            <thead className="table-light">
              <tr>
                <th>Date & Time</th>
                <th>Patient</th>
                <th>Notes</th>
                <th>Status</th>
                <th className="text-center">Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((appt) => (
                <tr key={appt.id}>
                  <td>{new Date(appt.appointmentDateTime).toLocaleString()}</td>
                  <td>{appt.patient?.user?.name}</td>
                  <td>{appt.notes}</td>
                  <td>
                    <span
                      className={`badge rounded-pill px-3 py-2 ${
                        appt.status === "CONFIRMED"
                          ? "bg-success"
                          : appt.status === "REJECTED"
                          ? "bg-danger"
                          : appt.status === "COMPLETED"
                          ? "bg-info text-dark"
                          : appt.status === "CANCELLED"
                          ? "bg-warning text-dark"
                          : "bg-secondary"
                      }`}
                    >
                      {appt.status}
                    </span>
                  </td>
                  <td className="text-center">
                    {appt.status === "REQUESTED" && (
                      <>
                        <button
                          className="btn btn-sm btn-success me-2"
                          onClick={() => updateStatus(appt.id, "CONFIRMED")}
                          disabled={updatingId === appt.id}
                        >
                          Confirm
                        </button>
                        <button
                          className="btn btn-sm btn-danger"
                          onClick={() => updateStatus(appt.id, "REJECTED")}
                          disabled={updatingId === appt.id}
                        >
                          Reject
                        </button>
                      </>
                    )}

                    {["CONFIRMED", "COMPLETED"].includes(appt.status) && (
                      <>
                        <Link
                          to={`/doctor/consultation/${appt.id}`}
                          className="btn btn-outline-primary btn-sm me-2"
                        >
                          {appt.status === "COMPLETED"
                            ? "View/Edit Consultation"
                            : "Add Consultation"}
                        </Link>

                        {appt.status === "COMPLETED" && (
                          <button
                            className="btn btn-outline-dark btn-sm"
                            onClick={() => handleOpenHistoryModal(appt)}
                          >
                            📋 Update History
                          </button>
                        )}
                      </>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {showHistoryModal && selectedAppointment && (
        <UpdateHistoryModal
          show={showHistoryModal}
          handleClose={() => setShowHistoryModal(false)}
          onSave={handleSaveHistory}
          patientName={selectedAppointment.patient.user?.name}
          existingHistory={selectedAppointment.patient.medicalHistory}
        />
      )}
    </div>
  );
};

export default DoctorAppointments;
