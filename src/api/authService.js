import axios from 'axios';

const API = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';
console.log("✅ API BASE URL used:", API);

export const login = async (email, password) => {
  console.log("🔁 Sending login request to:", `${API}/auth/login`);
  try {
    const response = await axios.post(`${API}/auth/login`, { email, password });
    console.log("✅ Login successful:", response.data);
    return response.data;
  } catch (error) {
    console.error("❌ Login failed:", error.response?.data || error.message);
    throw error;
  }
};

export const registerUser = async ({ name, email, password, role }) => {
  try {
    const response = await axios.post(`${API}/auth/register`, {
      name, email, password, role
    });
    return response.data;
  } catch (error) {
    console.error("❌ Registration failed:", error.response?.data || error.message);
    throw error;
  }
};

