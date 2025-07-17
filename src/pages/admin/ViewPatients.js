import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const ViewPatients = () => {
  const [patients, setPatients] = useState([]);
  const [filteredPatients, setFilteredPatients] = useState([]);

  const [searchTerm, setSearchTerm] = useState("");
  const [genderFilter, setGenderFilter] = useState("All");
  const [statusFilter, setStatusFilter] = useState("All");

  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const headers = { Authorization: `Bearer ${token}` };

  useEffect(() => {
    fetchPatients();
  }, []);

  useEffect(() => {
    applyFilters();
  }, [searchTerm, genderFilter, statusFilter, patients]);

  const fetchPatients = async () => {
    try {
      const res = await axios.get(
        `${process.env.REACT_APP_API_BASE_URL}/admin/patients`,
        { headers }
      );
      setPatients(res.data);
    } catch (err) {
      toast.error("Failed to load patients.");
    }
  };

  const applyFilters = () => {
    let filtered = [...patients];

    if (searchTerm) {
      filtered = filtered.filter(
        (p) =>
          p.user?.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
          p.user?.email.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    if (genderFilter !== "All") {
      filtered = filtered.filter((p) => p.gender === genderFilter);
    }

    if (statusFilter !== "All") {
      filtered = filtered.filter((p) => p.user?.status === statusFilter);
    }

    setFilteredPatients(filtered);
  };

  const toggleStatus = async (userId, currentStatus) => {
    const newStatus = currentStatus === "ACTIVE" ? "INACTIVE" : "ACTIVE";
    try {
      await axios.put(
        `${process.env.REACT_APP_API_BASE_URL}/admin/user/${userId}/status`,
        null,
        {
          params: { value: newStatus },
          headers,
        }
      );
      toast.success(`Status changed to ${newStatus}`);
      fetchPatients();
    } catch (err) {
      toast.error("Failed to update status");
    }
  };

  const formatDate = (dob) => {
    if (!dob) return "N/A";
    const d = new Date(dob);
    return d.toLocaleDateString("en-IN", {
      year: "numeric",
      month: "short",
      day: "numeric",
    });
  };

  return (
    <div className="container mt-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h3 className="text-primary fw-bold">Registered Patients</h3>
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
            className="form-control"
            placeholder="Search by name or email..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <div className="col-md-4">
          <select
            className="form-select"
            value={genderFilter}
            onChange={(e) => setGenderFilter(e.target.value)}
          >
            <option value="All">All Genders</option>
            <option value="MALE">MALE</option>
            <option value="FEMALE">FEMALE</option>
            <option value="OTHER">OTHER</option>
          </select>
        </div>
        <div className="col-md-4">
          <select
            className="form-select"
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
          >
            <option value="All">All Status</option>
            <option value="ACTIVE">ACTIVE</option>
            <option value="INACTIVE">INACTIVE</option>
          </select>
        </div>
      </div>

      {filteredPatients.length === 0 ? (
        <div className="alert alert-info">No patients match the filters.</div>
      ) : (
        <div className="table-responsive">
          <table className="table table-hover table-bordered align-middle shadow-sm">
            <thead className="table-light text-center">
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Contact</th>
                <th>Gender</th>
                <th>DOB</th>
                <th>Medical History</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {filteredPatients.map((p) => (
                <tr key={p.id}>
                  <td>{p.user?.name}</td>
                  <td>{p.user?.email}</td>
                  <td>{p.contactNumber || "N/A"}</td>
                  <td>{p.gender}</td>
                  <td>{formatDate(p.dob)}</td>
                  <td>{p.medicalHistory || "None"}</td>
                  <td className="text-center">
                    <span
                      className={`badge rounded-pill px-3 py-2 ${
                        p.user?.status === "ACTIVE"
                          ? "bg-success"
                          : "bg-secondary"
                      }`}
                      style={{ cursor: "pointer" }}
                      title="Click to toggle status"
                      onClick={() => toggleStatus(p.user?.id, p.user?.status)}
                    >
                      {p.user?.status}
                    </span>
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

export default ViewPatients;
