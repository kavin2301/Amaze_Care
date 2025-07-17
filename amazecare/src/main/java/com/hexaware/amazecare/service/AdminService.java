package com.hexaware.amazecare.service;

import com.hexaware.amazecare.dto.*;
import com.hexaware.amazecare.entity.*;

import java.util.List;

public interface AdminService {
    List<User> getAllUsers();
    List<Patient> getAllPatients();
    List<Doctor> getAllDoctors();
    List<Appointment> getAllAppointments();

    String createUserWithRole(RegisterRequest request, Role role);

    String deleteUser(Long id);
    String deletePatient(Long id);
    String deleteDoctor(Long id);
    String deleteAppointment(Long id);
    String deleteConsultation(Long id);
    String deletePrescriptionsByConsultationId(Long consultationId);
    String deleteTestsByConsultationId(Long consultationId);
    String deletePatientCascade(Long id);
    String deleteDoctorCascade(Long id);

    String updateUser(Long id, UpdateUserRequest request);
    String updatePatient(Long id, UpdatePatientRequest request);
    String updateDoctor(Long id, UpdateDoctorRequest request);
    String toggleUserStatus(Long id, Status status);
}
