import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import AdminDashboard from './pages/admin/AdminDashboard';
import ManageDoctors from './pages/admin/ManageDoctors';
import ViewPatients from './pages/admin/ViewPatients';
import AllAppointments from './pages/admin/AllAppointments';
import CreateAdmin from './pages/admin/CreateAdmin';
import DoctorDashboard from './pages/doctor/DoctorDashboard';
import DoctorAppointments from './pages/doctor/DoctorAppointments'; 
import DoctorProfile from './pages/doctor/DoctorProfile'; 
import ConsultationForm from './pages/doctor/ConsultationForm';
import PatientDashboard from './pages/patient/PatientDashboard';
import PatientProfile from './pages/patient/PatientProfile';
import BookAppointment from './pages/patient/BookAppointment';
import MyAppointments from './pages/patient/MyAppointments';
import Prescriptions from './pages/patient/Prescriptions';
import TestRecommendations from './pages/patient/TestRecommendations';
import ProtectedRoute from './components/ProtectedRoute';
import Navbar from './components/Navbar';
import { ToastContainer } from 'react-toastify';
import Home from './pages/Home';

function App() {
  return (
    <>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        <Route element={<ProtectedRoute allowedRoles={['ADMIN']} />}>
          <Route path="/admin/dashboard" element={<AdminDashboard />} />
          <Route path="/admin/manage-doctors" element={<ManageDoctors />} />
          <Route path="/admin/view-patients" element={<ViewPatients />} />
          <Route path="/admin/view-appointments" element={<AllAppointments />} />
          <Route path="/admin/create-admin" element={<CreateAdmin />} />
        </Route>


        <Route element={<ProtectedRoute allowedRoles={['DOCTOR']} />}>
          <Route path="/doctor/dashboard" element={<DoctorDashboard />} />
          <Route path="/doctor/appointments" element={<DoctorAppointments />} /> 
          <Route path="/doctor/consultation/:appointmentId" element={<ConsultationForm />} />
          <Route path="/doctor/profile" element={<DoctorProfile />} />
        </Route>

        <Route element={<ProtectedRoute allowedRoles={['PATIENT']} />}>
          <Route path="/patient/dashboard" element={<PatientDashboard />} />
          <Route path="/patient/profile" element={<PatientProfile />} />
          <Route path="/patient/book-appointment" element={<BookAppointment />} />
          <Route path="/patient/appointments" element={<MyAppointments />} />
          <Route path="/patient/prescriptions" element={<Prescriptions />} />
          <Route path="/patient/tests" element={<TestRecommendations />} />
        </Route>

       
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>

      <ToastContainer />
    </>
  );
}

export default App;
