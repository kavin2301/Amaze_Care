import axios from "axios";

const API = process.env.REACT_APP_API_BASE_URL;
const token = localStorage.getItem("token");
const headers = { Authorization: `Bearer ${token}` };


export const getAllDoctors = async () => {
  const res = await axios.get(`${API}/admin/doctors`, { headers });
  return res.data;
};

export const createDoctorUser = async (payload) => {
  return await axios.post(`${API}/admin/create-doctor`, payload, { headers });
};

export const updateDoctorProfile = async (doctorId, payload) => {
  return await axios.put(`${API}/admin/doctor/${doctorId}`, payload, { headers });
};

export const updateDoctorUserName = async (userId, payload) => {
  return await axios.put(`${API}/admin/user/${userId}`, payload, { headers });
};

export const deleteDoctorCascade = async (doctorId) => {
  return await axios.delete(`${API}/admin/doctor/cascade/${doctorId}`, { headers });
};
