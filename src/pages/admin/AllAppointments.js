import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const AllAppointments = () => {
  const [appointments, setAppointments] = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [sortOrder, setSortOrder] = useState("desc");

  const navigate = useNavigate();

  const [filter, setFilter] = useState({
    doctor: "",
    patient: "",
    status: "",
  });

  useEffect(() => {
    fetchAppointments();
  }, []);

  const fetchAppointments = async () => {
    try {
      const token = localStorage.getItem("token");
      const res = await axios.get(
        `${process.env.REACT_APP_API_BASE_URL}/admin/appointments`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      const sorted = res.data.sort(
        (a, b) => new Date(b.appointmentDateTime) - new Date(a.appointmentDateTime)
      );
      setAppointments(sorted);
      setFiltered(sorted);
    } catch {
      toast.error("Failed to load appointments");
    }
  };

  const handleFilterChange = (e) => {
    const updated = { ...filter, [e.target.name]: e.target.value };
    setFilter(updated);
    applyFilter(updated, sortOrder);
  };

  const applyFilter = (f, order = sortOrder) => {
    let results = [...appointments];

    if (f.doctor.trim()) {
      results = results.filter(
        (a) =>
          a.doctor?.user?.name?.toLowerCase().includes(f.doctor.toLowerCase()) ||
          a.doctor?.user?.email?.toLowerCase().includes(f.doctor.toLowerCase())
      );
    }

    if (f.patient.trim()) {
      results = results.filter(
        (a) =>
          a.patient?.user?.name?.toLowerCase().includes(f.patient.toLowerCase()) ||
          a.patient?.user?.email?.toLowerCase().includes(f.patient.toLowerCase())
      );
    }

    if (f.status.trim()) {
      results = results.filter((a) => a.status === f.status);
    }

   
    results.sort((a, b) => {
      const dateA = new Date(a.appointmentDateTime);
      const dateB = new Date(b.appointmentDateTime);
      return order === "asc" ? dateA - dateB : dateB - dateA;
    });

    setFiltered(results);
  };

  const toggleSort = () => {
    const newOrder = sortOrder === "asc" ? "desc" : "asc";
    setSortOrder(newOrder);
    applyFilter(filter, newOrder);
  };

  return (
    <div className="container mt-5">
     
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h3 className="text-primary fw-bold">All Appointments</h3>
        <button
          className="btn btn-outline-secondary"
          onClick={() => navigate("/admin/dashboard")}
        >
          ← Back to Dashboard
        </button>
      </div>

   
      <div className="row g-3 mb-4">
        <div className="col-md-4">
          <input
            type="text"
            name="doctor"
            className="form-control"
            placeholder="Filter by Doctor Name or Email"
            value={filter.doctor}
            onChange={handleFilterChange}
          />
        </div>
        <div className="col-md-4">
          <input
            type="text"
            name="patient"
            className="form-control"
            placeholder="Filter by Patient Name or Email"
            value={filter.patient}
            onChange={handleFilterChange}
          />
        </div>
        <div className="col-md-4">
          <select
            name="status"
            className="form-select"
            value={filter.status}
            onChange={handleFilterChange}
          >
            <option value="">-- Filter by Status --</option>
            <option value="REQUESTED">Requested</option>
            <option value="CONFIRMED">Confirmed</option>
            <option value="REJECTED">Rejected</option>
            <option value="CANCELLED">Cancelled</option>
            <option value="COMPLETED">Completed</option>
          </select>
        </div>
      </div>

     
      {filtered.length === 0 ? (
        <div className="alert alert-info">No appointments match the filters.</div>
      ) : (
        <div className="table-responsive">
          <table className="table table-hover table-bordered align-middle shadow-sm">
            <thead className="table-light text-center">
              <tr>
                <th onClick={toggleSort} style={{ cursor: "pointer" }}>
                  Date & Time{" "}
                  {sortOrder === "asc" ? (
                    <span title="Oldest First">🔼</span>
                  ) : (
                    <span title="Newest First">🔽</span>
                  )}
                </th>
                <th>Doctor</th>
                <th>Patient</th>
                <th>Status</th>
                <th>Notes</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((appt) => (
                <tr key={appt.id}>
                  <td>{new Date(appt.appointmentDateTime).toLocaleString()}</td>
                  <td>
                    {appt.doctor?.user?.name}
                    <br />
                    <small className="text-muted">{appt.doctor?.user?.email}</small>
                  </td>
                  <td>
                    {appt.patient?.user?.name}
                    <br />
                    <small className="text-muted">{appt.patient?.user?.email}</small>
                  </td>
                  <td className="text-center">
                    <span
                      className={`badge rounded-pill px-3 py-2 bg-${getStatusColor(
                        appt.status
                      )}`}
                    >
                      {appt.status}
                    </span>
                  </td>
                  <td>{appt.notes || "-"}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};


const getStatusColor = (status) => {
  switch (status) {
    case "CONFIRMED":
      return "success";
    case "REJECTED":
      return "danger";
    case "CANCELLED":
      return "warning text-dark";
    case "COMPLETED":
      return "info";
    default:
      return "secondary";
  }
};

export default AllAppointments;
