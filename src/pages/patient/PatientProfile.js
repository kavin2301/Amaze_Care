import React, { useEffect, useState } from "react";
import { getPatientProfile, updatePatientProfile } from "../../api/patientService";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const PatientProfile = () => {
  const navigate = useNavigate();

  const [profile, setProfile] = useState({
    name: "",
    gender: "",
    dob: "",
    contactNumber: "",
    medicalHistory: "",
  });

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const data = await getPatientProfile();
        setProfile({
          name: data.user?.name || "",
          gender: data.gender || "",
          dob: data.dob || "",
          contactNumber: data.contactNumber || "",
          medicalHistory: data.medicalHistory || "",
        });
      } catch (err) {
        toast.error("❌ Failed to load profile");
      }
    };
    fetchProfile();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProfile((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await updatePatientProfile(profile);
      toast.success("✅ Profile updated successfully");
    } catch (err) {
      toast.error("❌ Update failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-5" style={{ maxWidth: "700px" }}>
      <div className="card shadow p-4">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h3 className="text-primary mb-0">🙍‍♂️ Edit Profile</h3>
          <button
            className="btn btn-outline-secondary"
            onClick={() => navigate("/patient/dashboard")}
          >
            ← Back to Dashboard
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label fw-semibold">Full Name</label>
            <input
              type="text"
              className="form-control"
              name="name"
              value={profile.name}
              onChange={handleChange}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label fw-semibold">Gender</label>
            <select
              className="form-select"
              name="gender"
              value={profile.gender}
              onChange={handleChange}
              required
            >
              <option value="">Select Gender</option>
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
              <option value="OTHER">Other</option>
            </select>
          </div>

          <div className="mb-3">
            <label className="form-label fw-semibold">Date of Birth</label>
            <input
              type="date"
              className="form-control"
              name="dob"
              value={profile.dob}
              onChange={handleChange}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label fw-semibold">Contact Number</label>
            <input
              type="tel"
              pattern="[0-9]{10}"
              title="Enter a 10-digit number"
              className="form-control"
              name="contactNumber"
              value={profile.contactNumber}
              onChange={handleChange}
              required
            />
          </div>

          <div className="mb-4">
            <label className="form-label fw-semibold">Medical History</label>
            <textarea
              className="form-control"
              rows={4}
              value={profile.medicalHistory}
              readOnly
              style={{ backgroundColor: "#f8f9fa", cursor: "not-allowed" }}
            />
            <small className="text-muted">This section is managed by your doctor.</small>
          </div>

          <button className="btn btn-success w-100" type="submit" disabled={loading}>
            {loading ? "Saving..." : "✅ Update Profile"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default PatientProfile;
