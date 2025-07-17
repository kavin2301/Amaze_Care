import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import DoctorFormModal from "./components/DoctorFormModal";
import StatusToggleButton from "./components/StatusToggleButton";
import { useNavigate } from "react-router-dom";

const ManageDoctors = () => {
  const [doctors, setDoctors] = useState([]);
  const [filteredDoctors, setFilteredDoctors] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedDoctor, setSelectedDoctor] = useState(null);
  const [modalMode, setModalMode] = useState("add");

  const [searchTerm, setSearchTerm] = useState("");
  const [specialtyFilter, setSpecialtyFilter] = useState("All");
  const [statusFilter, setStatusFilter] = useState("All");

  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const headers = { Authorization: `Bearer ${token}` };

  useEffect(() => {
    fetchDoctors();
  }, []);

  const fetchDoctors = async () => {
    try {
      const res = await axios.get(`${process.env.REACT_APP_API_BASE_URL}/admin/doctors`, { headers });
      setDoctors(res.data);
      setFilteredDoctors(res.data);
    } catch (err) {
      toast.error("Failed to load doctors");
    }
  };

  useEffect(() => {
    applyFilters();
  }, [searchTerm, specialtyFilter, statusFilter, doctors]);

  const applyFilters = () => {
    let filtered = [...doctors];

    if (searchTerm) {
      filtered = filtered.filter(
        (doc) =>
          doc.user.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
          doc.user.email.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    if (specialtyFilter !== "All") {
      filtered = filtered.filter((doc) => doc.specialty === specialtyFilter);
    }

    if (statusFilter !== "All") {
      filtered = filtered.filter((doc) => doc.user.status === statusFilter);
    }

    setFilteredDoctors(filtered);
  };

  const getUniqueSpecialties = () => {
    const specialties = doctors.map((doc) => doc.specialty);
    return ["All", ...new Set(specialties)];
  };

  const handleAdd = () => {
    setSelectedDoctor(null);
    setModalMode("add");
    setShowModal(true);
  };

  const handleEdit = (doctor) => {
    setSelectedDoctor(doctor);
    setModalMode("edit");
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this doctor?")) {
      try {
        await axios.delete(`${process.env.REACT_APP_API_BASE_URL}/admin/doctor/cascade/${id}`, { headers });
        toast.success("Doctor deleted successfully");
        fetchDoctors();
      } catch {
        toast.error("Failed to delete doctor");
      }
    }
  };

  const handleSave = async (formData) => {
    try {
      if (modalMode === "add") {
        await axios.post(`${process.env.REACT_APP_API_BASE_URL}/admin/create-doctor`, {
          name: formData.name,
          email: formData.email,
          password: formData.password,
          role: "DOCTOR",
        }, { headers });

        const allDoctors = await axios.get(`${process.env.REACT_APP_API_BASE_URL}/admin/doctors`, { headers });
        const newDoctor = allDoctors.data.find(d => d.user.email === formData.email);
        if (newDoctor) {
          await axios.put(`${process.env.REACT_APP_API_BASE_URL}/admin/doctor/${newDoctor.id}`, {
            specialty: formData.specialty,
            experience: formData.experience,
            qualification: formData.qualification,
            designation: formData.designation,
          }, { headers });
        }

        toast.success("Doctor added successfully!");
      } else if (modalMode === "edit" && selectedDoctor) {
        await axios.put(`${process.env.REACT_APP_API_BASE_URL}/admin/user/${selectedDoctor.user.id}`, {
          name: formData.name,
        }, { headers });

        await axios.put(`${process.env.REACT_APP_API_BASE_URL}/admin/doctor/${selectedDoctor.id}`, {
          specialty: formData.specialty,
          experience: formData.experience,
          qualification: formData.qualification,
          designation: formData.designation,
        }, { headers });

        toast.success("Doctor updated successfully!");
      }

      setShowModal(false);
      fetchDoctors();
    } catch (err) {
      toast.error("Failed to save doctor");
    }
  };

  const toggleStatus = async (userId, newStatus) => {
    try {
      await axios.put(
        `${process.env.REACT_APP_API_BASE_URL}/admin/user/${userId}/status`,
        null,
        { params: { value: newStatus }, headers }
      );
      toast.success(`User status updated to ${newStatus}`);
      fetchDoctors();
    } catch {
      toast.error("Failed to update status");
    }
  };

  return (
    <div className="container mt-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h3 className="text-primary fw-bold">Manage Doctors</h3>
        <div className="d-flex gap-2">
          <button
            className="btn btn-outline-secondary"
            onClick={() => navigate("/admin/dashboard")}
          >
            ← Back to Dashboard
          </button>
          <button className="btn btn-success" onClick={handleAdd}>
            ➕ Add Doctor
          </button>
        </div>
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
            value={specialtyFilter}
            onChange={(e) => setSpecialtyFilter(e.target.value)}
          >
            {getUniqueSpecialties().map((specialty) => (
              <option key={specialty} value={specialty}>
                {specialty}
              </option>
            ))}
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

      <div className="table-responsive">
        <table className="table table-hover align-middle table-bordered shadow-sm">
          <thead className="table-light text-center">
            <tr>
              <th>Name</th>
              <th>Email</th>
              <th>Specialty</th>
              <th>Experience</th>
              <th>Qualification</th>
              <th>Designation</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filteredDoctors.length > 0 ? (
              filteredDoctors.map((doc) => (
                <tr key={doc.id}>
                  <td>{doc.user?.name}</td>
                  <td>{doc.user?.email}</td>
                  <td>{doc.specialty}</td>
                  <td>{doc.experience} yrs</td>
                  <td>{doc.qualification}</td>
                  <td>{doc.designation}</td>
                  <td className="text-center">
                    <StatusToggleButton
                      status={doc.user.status}
                      onToggle={(newStatus) => toggleStatus(doc.user.id, newStatus)}
                    />
                  </td>
                  <td className="text-center">
                    <button
                      className="btn btn-sm btn-outline-primary me-2"
                      onClick={() => handleEdit(doc)}
                    >
                      ✏️ Edit
                    </button>
                    <button
                      className="btn btn-sm btn-outline-danger"
                      onClick={() => handleDelete(doc.id)}
                    >
                      🗑️ Delete
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="8" className="text-center text-muted">
                  No doctors match the selected filters.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <DoctorFormModal
        show={showModal}
        handleClose={() => setShowModal(false)}
        handleSave={handleSave}
        doctor={selectedDoctor}
        mode={modalMode}
      />
    </div>
  );
};

export default ManageDoctors;
