package com.hexaware.amazecare.controller;

import com.hexaware.amazecare.dto.RegisterRequest;
import com.hexaware.amazecare.dto.UpdateDoctorRequest;
import com.hexaware.amazecare.dto.UpdatePatientRequest;
import com.hexaware.amazecare.dto.UpdateUserRequest;
import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/patients")
    public List<Patient> getAllPatients() {
        return adminService.getAllPatients();
    }

    @GetMapping("/doctors")
    public List<Doctor> getAllDoctors() {
        return adminService.getAllDoctors();
    }

    @GetMapping("/appointments")
    public List<Appointment> getAllAppointments() {
        return adminService.getAllAppointments();
    }

    @PostMapping("/create-doctor")
    public String createDoctor(@RequestBody RegisterRequest request) {
        return adminService.createUserWithRole(request, Role.DOCTOR);
    }

    @PostMapping("/create-admin")
    public String createAdmin(@RequestBody RegisterRequest request) {
        return adminService.createUserWithRole(request, Role.ADMIN);
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Long id) {
        return adminService.deleteUser(id);
    }

    @DeleteMapping("/patient/{id}")
    public String deletePatient(@PathVariable Long id) {
        return adminService.deletePatient(id);
    }

    @DeleteMapping("/doctor/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        return adminService.deleteDoctor(id);
    }

    @DeleteMapping("/appointment/{id}")
    public String deleteAppointment(@PathVariable Long id) {
        return adminService.deleteAppointment(id);
    }

    @DeleteMapping("/consultation/{id}")
    public String deleteConsultation(@PathVariable Long id) {
        return adminService.deleteConsultation(id);
    }

    @DeleteMapping("/consultation/{consultationId}/prescriptions")
    public String deletePrescriptions(@PathVariable Long consultationId) {
        return adminService.deletePrescriptionsByConsultationId(consultationId);
    }

    @DeleteMapping("/consultation/{consultationId}/tests")
    public String deleteTests(@PathVariable Long consultationId) {
        return adminService.deleteTestsByConsultationId(consultationId);
    }

    @DeleteMapping("/patient/cascade/{id}")
    public String deletePatientCascade(@PathVariable Long id) {
        return adminService.deletePatientCascade(id);
    }

    @DeleteMapping("/doctor/cascade/{id}")
    public String deleteDoctorCascade(@PathVariable Long id) {
        return adminService.deleteDoctorCascade(id);
    }
    

    @PutMapping("/user/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        return adminService.updateUser(id, request);
    }

    @PutMapping("/patient/{id}")
    public String updatePatient(@PathVariable Long id, @RequestBody UpdatePatientRequest request) {
        return adminService.updatePatient(id, request);
    }

    @PutMapping("/doctor/{id}")
    public String updateDoctor(@PathVariable Long id, @RequestBody UpdateDoctorRequest request) {
        return adminService.updateDoctor(id, request);
    }

    @PutMapping("/user/{id}/status")
    public String toggleUserStatus(@PathVariable Long id, @RequestParam Status value) {
        return adminService.toggleUserStatus(id, value);
    }
}
