import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const TestRecommendations = () => {
  const [testGroups, setTestGroups] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetchTests();
  }, []);

  const fetchTests = async () => {
    setLoading(true);
    const token = localStorage.getItem("token");
    const headers = { Authorization: `Bearer ${token}` };

    try {
      const apptRes = await axios.get(
        `${process.env.REACT_APP_API_BASE_URL}/appointments/patient`,
        { headers }
      );

      const testResults = [];

      for (const appt of apptRes.data) {
        try {
          const consultRes = await axios.get(
            `${process.env.REACT_APP_API_BASE_URL}/consultations/appointment/${appt.id}`,
            { headers }
          );

          const testsRes = await axios.get(
            `${process.env.REACT_APP_API_BASE_URL}/consultations/${consultRes.data.id}/tests`,
            { headers }
          );

          if (testsRes.data.length > 0) {
            testResults.push({
              doctor: appt.doctor?.user?.name || "Unknown",
              email: appt.doctor?.user?.email || "N/A",
              date: appt.appointmentDateTime,
              tests: testsRes.data,
            });
          }
        } catch {
          
        }
      }

      
      testResults.sort((a, b) => new Date(b.date) - new Date(a.date));
      setTestGroups(testResults);
    } catch (err) {
      console.error("Error fetching test recommendations", err);
      toast.error("Failed to load test recommendations.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h3 className="text-primary">🧪 My Test Recommendations</h3>
        <button
          className="btn btn-outline-secondary"
          onClick={() => navigate("/patient/dashboard")}
        >
          ← Back to Dashboard
        </button>
      </div>

      {loading ? (
        <div className="alert alert-secondary">Loading test recommendations...</div>
      ) : testGroups.length === 0 ? (
        <div className="alert alert-info">No test recommendations found.</div>
      ) : (
        testGroups.map((group, idx) => (
          <div key={idx} className="card mb-4 shadow-sm border-0">
            <div className="card-header bg-light d-flex justify-content-between align-items-center">
              <div>
                <strong>{group.doctor}</strong>
                <br />
                <small className="text-muted">{group.email}</small>
              </div>
              <small className="text-muted">
                {new Date(group.date).toLocaleString()}
              </small>
            </div>

            <ul className="list-group list-group-flush">
              {group.tests.map((test, tIdx) => (
                <li key={tIdx} className="list-group-item">
                  <strong>{test.testName}</strong>
                  <div className="text-muted small mt-1">
                    <strong>Reason:</strong> {test.description || "N/A"}
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

export default TestRecommendations;
