import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { toast } from 'react-toastify';

const ConsultationForm = () => {
  const { appointmentId } = useParams();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [consultation, setConsultation] = useState(null);

  const [form, setForm] = useState({
    symptoms: '',
    physicalExamination: '',
    treatmentPlan: ''
  });

  const [prescriptions, setPrescriptions] = useState([
    { medicineName: '', dosage: '', timing: '', instruction: '' }
  ]);
  const [tests, setTests] = useState([{ testName: '', description: '' }]);

  const [existingPrescriptions, setExistingPrescriptions] = useState([]);
  const [existingTests, setExistingTests] = useState([]);

  const token = localStorage.getItem('token');
  const headers = { Authorization: `Bearer ${token}` };

  useEffect(() => {
    fetchConsultation();
  }, []);

  const fetchConsultation = async () => {
    try {
      const res = await axios.get(`${process.env.REACT_APP_API_BASE_URL}/consultations/appointment/${appointmentId}`, { headers });
      setConsultation(res.data);
      setForm({
        symptoms: res.data.symptoms,
        physicalExamination: res.data.physicalExamination,
        treatmentPlan: res.data.treatmentPlan
      });

      const [presRes, testRes] = await Promise.all([
        axios.get(`${process.env.REACT_APP_API_BASE_URL}/consultations/${res.data.id}/prescriptions`, { headers }),
        axios.get(`${process.env.REACT_APP_API_BASE_URL}/consultations/${res.data.id}/tests`, { headers })
      ]);

      setExistingPrescriptions(presRes.data);
      setExistingTests(testRes.data);

    } catch {
      console.log("🔍 No consultation found. Creating a new one.");
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmitConsultation = async (e) => {
  e.preventDefault();
  try {
    if (consultation) {
      
      await axios.put(
        `${process.env.REACT_APP_API_BASE_URL}/consultations/${consultation.id}`,
        form,
        { headers }
      );
      toast.success("Consultation updated!");
    } else {
      // Create
      await axios.post(
        `${process.env.REACT_APP_API_BASE_URL}/consultations?appointmentId=${appointmentId}`,
        form,
        { headers }
      );
      toast.success("Consultation created!");
    }
    fetchConsultation();
  } catch {
    toast.error("❌ Failed to save consultation");
  }
};

  const handlePrescriptionChange = (index, field, value) => {
    const updated = [...prescriptions];
    updated[index][field] = value;
    setPrescriptions(updated);
  };

  const handleTestChange = (index, field, value) => {
    const updated = [...tests];
    updated[index][field] = value;
    setTests(updated);
  };

  const addPrescriptionRow = () =>
    setPrescriptions([...prescriptions, { medicineName: '', dosage: '', timing: '', instruction: '' }]);
  const addTestRow = () =>
    setTests([...tests, { testName: '', description: '' }]);

  const handleSavePrescriptions = async () => {
    try {
      await axios.post(
        `${process.env.REACT_APP_API_BASE_URL}/consultations/${consultation.id}/prescriptions`,
        prescriptions,
        { headers }
      );
      toast.success("💊 Prescriptions saved");
      fetchConsultation();
    } catch {
      toast.error("❌ Failed to save prescriptions");
    }
  };

  const handleSaveTests = async () => {
    try {
      await axios.post(
        `${process.env.REACT_APP_API_BASE_URL}/consultations/${consultation.id}/tests`,
        tests,
        { headers }
      );
      toast.success("🧪 Tests saved");
      fetchConsultation();
    } catch {
      toast.error("❌ Failed to save test recommendations");
    }
  };

  if (loading) return <div className="container mt-5">Loading...</div>;

  return (
    <div className="container mt-5" style={{ maxWidth: '950px' }}>
      <h3 className="mb-4 text-primary">📝 Consultation for Appointment #{appointmentId}</h3>

      
      <form onSubmit={handleSubmitConsultation}>
        <div className="mb-3">
          <label className="form-label fw-bold">Symptoms</label>
          <textarea className="form-control" name="symptoms" value={form.symptoms} onChange={handleChange} rows="3" required />
        </div>

        <div className="mb-3">
          <label className="form-label fw-bold">Physical Examination</label>
          <textarea className="form-control" name="physicalExamination" value={form.physicalExamination} onChange={handleChange} rows="3" required />
        </div>

        <div className="mb-3">
          <label className="form-label fw-bold">Treatment Plan</label>
          <textarea className="form-control" name="treatmentPlan" value={form.treatmentPlan} onChange={handleChange} rows="3" required />
        </div>

        <button type="submit" className="btn btn-primary w-100 mb-4">
          {consultation ? "Update Consultation" : "Save Consultation"}
        </button>
      </form>

     
      {existingPrescriptions.length > 0 && (
        <>
          <h5 className="fw-semibold">💊 Existing Prescriptions</h5>
          <ul className="list-group mb-3">
            {existingPrescriptions.map((pres, idx) => (
              <li key={idx} className="list-group-item">
                <strong>{pres.medicineName}</strong> – {pres.dosage}
                <div className="small text-muted">
                  <strong>Timing:</strong> {pres.timing || "N/A"} <br />
                  <strong>Instructions:</strong> {pres.instruction || "N/A"}
                </div>
              </li>
            ))}
          </ul>
        </>
      )}


      <h5 className="fw-semibold">Add New Prescriptions</h5>
      {prescriptions.map((p, idx) => (
        <div className="row g-2 mb-2" key={idx}>
          <div className="col-md-3">
            <input type="text" className="form-control" placeholder="Medicine"
              value={p.medicineName} onChange={(e) => handlePrescriptionChange(idx, 'medicineName', e.target.value)} />
          </div>
          <div className="col-md-2">
            <input type="text" className="form-control" placeholder="Dosage"
              value={p.dosage} onChange={(e) => handlePrescriptionChange(idx, 'dosage', e.target.value)} />
          </div>
          <div className="col-md-2">
            <input type="text" className="form-control" placeholder="Timing"
              value={p.timing} onChange={(e) => handlePrescriptionChange(idx, 'timing', e.target.value)} />
          </div>
          <div className="col-md-5">
            <input type="text" className="form-control" placeholder="Instructions"
              value={p.instruction} onChange={(e) => handlePrescriptionChange(idx, 'instruction', e.target.value)} />
          </div>
        </div>
      ))}
      <div className="d-flex justify-content-between mb-4">
        <button onClick={addPrescriptionRow} className="btn btn-sm btn-secondary">➕ Add Row</button>
        <button onClick={handleSavePrescriptions} className="btn btn-outline-success">💾 Save Prescriptions</button>
      </div>

     
      {existingTests.length > 0 && (
        <>
          <h5 className="fw-semibold">🧪 Existing Test Recommendations</h5>
          <ul className="list-group mb-3">
            {existingTests.map((t, idx) => (
              <li key={idx} className="list-group-item">
                <strong>{t.testName}</strong> – <em>{t.description}</em>
              </li>
            ))}
          </ul>
        </>
      )}

    
      <h5 className="fw-semibold">Add New Test Recommendations</h5>
      {tests.map((t, idx) => (
        <div className="row g-2 mb-2" key={idx}>
          <div className="col-md-4">
            <input type="text" className="form-control" placeholder="Test Name"
              value={t.testName} onChange={(e) => handleTestChange(idx, 'testName', e.target.value)} />
          </div>
          <div className="col-md-8">
            <input type="text" className="form-control" placeholder="Description"
              value={t.description} onChange={(e) => handleTestChange(idx, 'description', e.target.value)} />
          </div>
        </div>
      ))}
      <div className="d-flex justify-content-between mb-5">
        <button onClick={addTestRow} className="btn btn-sm btn-secondary">➕ Add Row</button>
        <button onClick={handleSaveTests} className="btn btn-outline-primary">💾 Save Test Recommendations</button>
      </div>

     
      <div className="text-center">
        <button className="btn btn-outline-dark" onClick={() => navigate("/doctor/appointments")}>
          ← Back to Appointments
        </button>
      </div>
    </div>
  );
};

export default ConsultationForm;
