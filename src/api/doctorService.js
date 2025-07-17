
import axios from 'axios';

const API = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';

const getTokenHeader = () => ({
  headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
});

export const getDoctorProfile = async () => {
  const res = await axios.get(`${API}/doctor/profile`, getTokenHeader());
  return res.data;
};

export const updateDoctorProfile = async (updatedDoctor) => {
  const res = await axios.put(`${API}/doctor/profile`, updatedDoctor, getTokenHeader());
  return res.data;
};
