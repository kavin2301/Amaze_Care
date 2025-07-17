import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import BookModal from './components/BookModal';

const API = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';

const BookAppointment = () => {
  const [doctors, setDoctors] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredDoctors, setFilteredDoctors] = useState([]);
  const [selectedDoctor, setSelectedDoctor] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    fetchDoctors();
  }, []);

  const fetchDoctors = async () => {
    try {
      const res = await axios.get(`${API}/doctors`);
      setDoctors(res.data);
      setFilteredDoctors(res.data);
    } catch (error) {
      console.error("❌ Doctor fetch failed", error);
      toast.error('Failed to load doctors.');
    }
  };

  const handleSearchChange = (e) => {
    const value = e.target.value;
    setSearchTerm(value);
    const filtered = doctors.filter((doc) =>
      doc.specialty.toLowerCase().includes(value.toLowerCase())
    );
    setFilteredDoctors(filtered);
  };

  const handleBookClick = (doctor) => {
    setSelectedDoctor(doctor);
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setSelectedDoctor(null);
  };

  const handleBookingSuccess = () => {
    toast.success("✅ Appointment booked!");
    handleCloseModal();
  };

  return (
    <div className="container py-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="fw-bold text-primary">📅 Book Appointment</h2>
        <button
          className="btn btn-outline-secondary"
          onClick={() => navigate("/patient/dashboard")}
        >
          ← Back to Dashboard
        </button>
      </div>

      <div className="mb-4">
        <input
          type="text"
          className="form-control shadow-sm"
          placeholder="🔍 Search by specialty..."
          value={searchTerm}
          onChange={handleSearchChange}
        />
      </div>

      {filteredDoctors.length === 0 ? (
        <div className="alert alert-warning">No doctors found.</div>
      ) : (
        <div className="row g-4">
          {filteredDoctors.map((doc) => (
            <div className="col-md-4" key={doc.id}>
              <div
                className="card h-100 shadow border-0 rounded-4"
                style={{ transition: "transform 0.2s" }}
                onMouseEnter={(e) => (e.currentTarget.style.transform = "scale(1.02)")}
                onMouseLeave={(e) => (e.currentTarget.style.transform = "scale(1)" )}
              >
                <div className="card-body">
                  <h5 className="card-title text-primary fw-semibold mb-2">
                    {doc.user?.name}
                  </h5>
                  <ul className="list-unstyled small text-muted mb-3">
                    <li><strong>Email:</strong> <span className="text-break">{doc.user?.email}</span></li>
                    <li><strong>Specialty:</strong> {doc.specialty}</li>
                    <li><strong>Experience:</strong> {doc.experience} years</li>
                    <li><strong>Qualification:</strong> {doc.qualification}</li>
                    <li><strong>Designation:</strong> {doc.designation}</li>
                  </ul>
                  <button
                    className="btn btn-success w-100"
                    onClick={() => handleBookClick(doc)}
                  >
                    Book Appointment
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {selectedDoctor && (
        <BookModal
          show={showModal}
          handleClose={handleCloseModal}
          doctor={selectedDoctor}
          onSuccess={handleBookingSuccess}
        />
      )}
    </div>
  );
};

export default BookAppointment;
