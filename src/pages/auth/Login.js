import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login as loginService } from "../../api/authService";
import { useAuth } from "../../context/AuthContext";
import { toast } from "react-toastify";

const Login = () => {
  const { login } = useAuth();
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const { token, role } = await loginService(email, password);
      login(token, role);
      toast.success("Login successful!");

      if (role === "ADMIN") navigate("/admin/dashboard");
      else if (role === "DOCTOR") navigate("/doctor/dashboard");
      else navigate("/patient/dashboard");
    } catch (error) {
      const message = error.response?.data?.message || "Invalid credentials!";
      if (message.toLowerCase().includes("inactive")) {
        toast.error("🚫 Account is inactive. Please contact admin.");
      } else {
        toast.error("❌ Invalid credentials!");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container py-5 d-flex justify-content-center align-items-center" style={{ minHeight: "80vh" }}>
      <div className="card shadow-sm p-4" style={{ width: "100%", maxWidth: "420px" }}>
        <h3 className="text-center mb-4 text-primary">🔐 Login to AmazeCare</h3>

        <form onSubmit={handleLogin}>
          <div className="mb-3">
            <label className="form-label fw-semibold">Email</label>
            <input
              type="email"
              className="form-control"
              placeholder="you@example.com"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>

          <div className="mb-3">
            <label className="form-label fw-semibold">Password</label>
            <input
              type="password"
              className="form-control"
              placeholder="********"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          <button
            type="submit"
            className="btn btn-primary w-100"
            disabled={loading}
          >
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>

        <div className="text-center mt-3">
          <small className="text-muted">
            Don't have an account?{" "}
            <a href="/register" className="text-decoration-none fw-semibold text-primary">
              Register here
            </a>
          </small>
        </div>
      </div>
    </div>
  );
};

export default Login;
