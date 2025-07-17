import React, { useEffect, useState } from 'react';
import { getDoctorProfile, updateDoctorProfile } from '../../api/doctorService';
import { toast } from 'react-toastify';
import { useNavigate } from "react-router-dom";

const DoctorProfile = () => {
  const [doctor, setDoctor] = useState({
    specialty: '',
    experience: '',
    qualification: '',
    designation: '',
    user: { name: '', email: '' }
  });

  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetchDoctor();
  }, []);

  const fetchDoctor = async () => {
    try {
      const data = await getDoctorProfile();
      setDoctor(data);
    } catch (err) {
      toast.error('Failed to load profile');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === 'name') {
      setDoctor(prev => ({ ...prev, user: { ...prev.user, name: value } }));
    } else {
      setDoctor(prev => ({ ...prev, [name]: value }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await updateDoctorProfile(doctor);
      toast.success("Profile updated");
    } catch (err) {
      toast.error("Update failed");
    }
  };

  if (loading) return <div className="container mt-5">Loading...</div>;

  return (
    <div className="container mt-5" style={{ maxWidth: "600px" }}>
      <h3 className="mb-4">My Profile</h3>

      <button
        className="btn btn-outline-secondary mb-4"
        onClick={() => navigate("/doctor/dashboard")}
      >
        ← Back to Dashboard
      </button>

      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Name</label>
          <input
            type="text"
            name="name"
            className="form-control"
            value={doctor.user?.name}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Specialty</label>
          <input
            type="text"
            name="specialty"
            className="form-control"
            value={doctor.specialty}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Experience (Years)</label>
          <input
            type="number"
            name="experience"
            className="form-control"
            value={doctor.experience}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Qualification</label>
          <input
            type="text"
            name="qualification"
            className="form-control"
            value={doctor.qualification}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Designation</label>
          <input
            type="text"
            name="designation"
            className="form-control"
            value={doctor.designation}
            onChange={handleChange}
            required
          />
        </div>

        <div className="d-grid">
          <button type="submit" className="btn btn-primary">
            Update Profile
          </button>
        </div>
      </form>
    </div>
  );
};

export default DoctorProfile;
