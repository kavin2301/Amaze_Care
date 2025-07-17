import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const Prescriptions = () => {
  const [prescriptions, setPrescriptions] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetchPrescriptions();
  }, []);

  const fetchPrescriptions = async () => {
    setLoading(true);
    const token = localStorage.getItem("token");
    const headers = { Authorization: `Bearer ${token}` };

    try {
      const apptRes = await axios.get(
        `${process.env.REACT_APP_API_BASE_URL}/appointments/patient`,
        { headers }
      );

      const fetchedPrescriptions = [];

      for (const appt of apptRes.data) {
        try {
          const consultRes = await axios.get(
            `${process.env.REACT_APP_API_BASE_URL}/consultations/appointment/${appt.id}`,
            { headers }
          );

          const prescriptionRes = await axios.get(
            `${process.env.REACT_APP_API_BASE_URL}/consultations/${consultRes.data.id}/prescriptions`,
            { headers }
          );

          if (prescriptionRes.data.length > 0) {
            fetchedPrescriptions.push({
              appointmentId: appt.id,
              doctor: appt.doctor?.user?.name || "Unknown",
              doctorEmail: appt.doctor?.user?.email || "N/A",
              date: appt.appointmentDateTime,
              prescriptions: prescriptionRes.data,
            });
          }
        } catch (err) {
          
        }
      }

      
      fetchedPrescriptions.sort(
        (a, b) => new Date(b.date) - new Date(a.date)
      );

      setPrescriptions(fetchedPrescriptions);
    } catch (err) {
      console.error("Error loading prescriptions", err);
      toast.error("Failed to load prescriptions.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h3 className="text-primary">💊 My Prescriptions</h3>
        <button
          className="btn btn-outline-secondary"
          onClick={() => navigate("/patient/dashboard")}
        >
          ← Back to Dashboard
        </button>
      </div>

      {loading ? (
        <div className="alert alert-secondary">Loading prescriptions...</div>
      ) : prescriptions.length === 0 ? (
        <div className="alert alert-info">No prescriptions available.</div>
      ) : (
        prescriptions.map((entry, index) => (
          <div key={index} className="card mb-4 shadow-sm border-0">
            <div className="card-header bg-light d-flex justify-content-between align-items-center">
              <div>
                <strong>{entry.doctor}</strong>
                <br />
                <small className="text-muted">{entry.doctorEmail}</small>
              </div>
              <small className="text-muted">
                {new Date(entry.date).toLocaleString()}
              </small>
            </div>

            <ul className="list-group list-group-flush">
              {entry.prescriptions.map((pres, idx) => (
                <li key={idx} className="list-group-item">
                  <div className="d-flex justify-content-between">
                    <strong>{pres.medicineName}</strong>
                    <span className="text-muted">{pres.dosage}</span>
                  </div>
                  <div className="mt-1 text-muted small">
                    <div>
                      <strong>Timing:</strong> {pres.timing || "N/A"}
                    </div>
                    <div>
                      <strong>Instructions:</strong> {pres.instruction || "N/A"}
                    </div>
                  </div>
                </li>
              ))}
            </ul>
          </div>
        ))
      )}
    </div>
  );
};

export default Prescriptions;
