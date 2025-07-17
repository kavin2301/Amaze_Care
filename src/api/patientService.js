import axios from 'axios';

const API = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';

export const getPatientProfile = async () => {
  const token = localStorage.getItem("token");
  const response = await axios.get(`${API}/patient/profile`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return response.data;
};

export const updatePatientProfile = async (updatedPatient) => {
  const token = localStorage.getItem("token");
  const response = await axios.put(`${API}/patient/profile`, updatedPatient, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return response.data;
};
