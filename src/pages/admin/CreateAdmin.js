import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const API = process.env.REACT_APP_API_BASE_URL || "http://localhost:8080/api";

const CreateAdmin = () => {
  const [form, setForm] = useState({ name: "", email: "", password: "" });
  const [admins, setAdmins] = useState([]);
  const [filteredAdmins, setFilteredAdmins] = useState([]);
  const [filters, setFilters] = useState({ email: "", status: "" });

  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const headers = { Authorization: `Bearer ${token}` };

  useEffect(() => {
    fetchAdmins();
  }, []);

  useEffect(() => {
    applyFilters();
  }, [filters, admins]);

  const fetchAdmins = async () => {
    try {
      const res = await axios.get(`${API}/admin/users`, { headers });
      const onlyAdmins = res.data.filter((user) => user.role === "ADMIN");
      setAdmins(onlyAdmins);
      setFilteredAdmins(onlyAdmins); // Initial filtered list
    } catch {
      toast.error("❌ Failed to load admins");
    }
  };

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.name || !form.email || !form.password) {
      toast.warning("Please fill all fields");
      return;
    }

    try {
      await axios.post(
        `${API}/admin/create-admin`,
        { ...form, role: "ADMIN" },
        { headers }
      );
      toast.success("✅ Admin created successfully");
      setForm({ name: "", email: "", password: "" });
      fetchAdmins();
    } catch {
      toast.error("❌ Failed to create admin");
    }
  };

  const toggleStatus = async (adminId, currentStatus) => {
    const newStatus = currentStatus === "ACTIVE" ? "INACTIVE" : "ACTIVE";
    try {
      await axios.put(`${API}/admin/user/${adminId}/status`, null, {
        params: { value: newStatus },
        headers,
      });
      toast.success(`🔄 Status changed to ${newStatus}`);
      fetchAdmins();
    } catch {
      toast.error("❌ Failed to update status");
    }
  };

  const handleFilterChange = (e) => {
    setFilters({ ...filters, [e.target.name]: e.target.value });
  };

  const applyFilters = () => {
    let result = [...admins];

    if (filters.email.trim()) {
      result = result.filter((admin) =>
        admin.email.toLowerCase().includes(filters.email.toLowerCase())
      );
    }

    if (filters.status) {
      result = result.filter((admin) => admin.status === filters.status);
    }

    setFilteredAdmins(result);
  };

  return (
    <div className="container mt-5" style={{ maxWidth: "900px" }}>
      {/* Header */}
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h3 className="text-primary fw-bold">🛡️ Create New Admin</h3>
        <button
          className="btn btn-outline-secondary"
          onClick={() => navigate("/admin/dashboard")}
        >
          ← Back to Dashboard
        </button>
      </div>

      {/* Form */}
      <div className="card mb-5 shadow-sm">
        <div className="card-body">
          <form onSubmit={handleSubmit}>
            <div className="row g-3">
              <div className="col-md-4">
                <label className="form-label">Name</label>
                <input
                  type="text"
                  name="name"
                  className="form-control"
                  placeholder="Enter admin name"
                  value={form.name}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="col-md-4">
                <label className="form-label">Email</label>
                <input
                  type="email"
                  name="email"
                  className="form-control"
                  placeholder="Enter admin email"
                  value={form.email}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="col-md-4">
                <label className="form-label">Password</label>
                <input
                  type="password"
                  name="password"
                  className="form-control"
                  placeholder="Enter secure password"
                  value={form.password}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>

            <div className="mt-4 text-end">
              <button type="submit" className="btn btn-success">
                ➕ Create Admin
              </button>
            </div>
          </form>
        </div>
      </div>

      {/* Filters */}
      <h4 className="mb-3">👤 Existing Admins</h4>
      <div className="row g-3 mb-3">
        <div className="col-md-6">
          <input
            type="text"
            name="email"
            className="form-control"
            placeholder="🔍 Filter by email"
            value={filters.email}
            onChange={handleFilterChange}
          />
        </div>
        <div className="col-md-6">
          <select
            name="status"
            className="form-select"
            value={filters.status}
            onChange={handleFilterChange}
          >
            <option value="">-- Filter by Status --</option>
            <option value="ACTIVE">ACTIVE</option>
            <option value="INACTIVE">INACTIVE</option>
          </select>
        </div>
      </div>

      {/* Admin List */}
      {filteredAdmins.length === 0 ? (
        <div className="alert alert-info">No matching admin users found.</div>
      ) : (
        <div className="table-responsive">
          <table className="table table-hover table-bordered shadow-sm">
            <thead className="table-light text-center">
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {filteredAdmins.map((admin) => (
                <tr key={admin.id}>
                  <td>{admin.name}</td>
                  <td>{admin.email}</td>
                  <td className="text-center">
                    <span
                      className={`badge rounded-pill px-3 py-2 ${
                        admin.status === "ACTIVE" ? "bg-success" : "bg-secondary"
                      }`}
                      style={{ cursor: "pointer" }}
                      title="Click to toggle status"
                      onClick={() => toggleStatus(admin.id, admin.status)}
                    >
                      {admin.status}
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

export default CreateAdmin;
