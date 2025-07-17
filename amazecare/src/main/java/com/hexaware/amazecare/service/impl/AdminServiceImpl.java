package com.hexaware.amazecare.service.impl;

import com.hexaware.amazecare.dto.RegisterRequest;
import com.hexaware.amazecare.dto.UpdateDoctorRequest;
import com.hexaware.amazecare.dto.UpdatePatientRequest;
import com.hexaware.amazecare.dto.UpdateUserRequest;
import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import com.hexaware.amazecare.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private TestRecommendationRepository testRecommendationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public String createUserWithRole(RegisterRequest request, Role role) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already exists.";
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        userRepository.save(user);
        
        if (role == Role.DOCTOR) {
            Doctor doctor = new Doctor();
            doctor.setUser(user);
            doctorRepository.save(doctor);
        }

        return role.name() + " created successfully.";
    }

    @Override
    public String deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return "User not found.";
        }
        userRepository.deleteById(id);
        return "User deleted successfully.";
    }

    @Override
    public String deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            return "Patient not found.";
        }
        patientRepository.deleteById(id);
        return "Patient deleted successfully.";
    }

    @Override
    public String deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            return "Doctor not found.";
        }
        doctorRepository.deleteById(id);
        return "Doctor deleted successfully.";
    }

    @Override
    public String deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            return "Appointment not found.";
        }
        appointmentRepository.deleteById(id);
        return "Appointment deleted successfully.";
    }

    @Override
    public String deleteConsultation(Long id) {
        if (!consultationRepository.existsById(id)) {
            return "Consultation not found.";
        }
        consultationRepository.deleteById(id);
        return "Consultation deleted successfully.";
    }

    @Override
    public String deletePrescriptionsByConsultationId(Long consultationId) {
        List<Prescription> prescriptions = prescriptionRepository.findByConsultationId(consultationId);
        if (prescriptions.isEmpty()) {
            return "No prescriptions found for this consultation.";
        }
        prescriptionRepository.deleteAll(prescriptions);
        return "Prescriptions deleted successfully.";
    }

    @Override
    public String deleteTestsByConsultationId(Long consultationId) {
        List<TestRecommendation> tests = testRecommendationRepository.findByConsultationId(consultationId);
        if (tests.isEmpty()) {
            return "No test recommendations found for this consultation.";
        }
        testRecommendationRepository.deleteAll(tests);
        return "Test recommendations deleted successfully.";
    }


    @Override
    public String deletePatientCascade(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<Appointment> appointments = appointmentRepository.findByPatient(patient);
        for (Appointment appt : appointments) {
            consultationRepository.findByAppointment(appt).ifPresent(consultation -> {
                prescriptionRepository.deleteAll(prescriptionRepository.findByConsultation(consultation));
                testRecommendationRepository.deleteAll(testRecommendationRepository.findByConsultation(consultation));
                consultationRepository.delete(consultation);
            });
            appointmentRepository.delete(appt);
        }

        patientRepository.delete(patient);
        return "Patient and all related records deleted successfully.";
    }

    @Override
    public String deleteDoctorCascade(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<Appointment> appointments = appointmentRepository.findByDoctor(doctor);
        for (Appointment appt : appointments) {
            consultationRepository.findByAppointment(appt).ifPresent(consultation -> {
                prescriptionRepository.deleteAll(prescriptionRepository.findByConsultation(consultation));
                testRecommendationRepository.deleteAll(testRecommendationRepository.findByConsultation(consultation));
                consultationRepository.delete(consultation);
            });
            appointmentRepository.delete(appt);
        }

        doctorRepository.delete(doctor);
        return "Doctor and all related records deleted successfully.";
    }
    
    @Override
    public String updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setStatus(request.getStatus());

        // Skip role change logic entirely for safety
        userRepository.save(user);
        return "User updated successfully.";
    }


    @Override
    public String updatePatient(Long id, UpdatePatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setDob(request.getDob());
        patient.setGender(request.getGender());
        patient.setContactNumber(request.getContactNumber());
        patient.setMedicalHistory(request.getMedicalHistory());
        patientRepository.save(patient);

        return "Patient profile updated.";
    }

    @Override
    public String updateDoctor(Long id, UpdateDoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        doctor.setSpecialty(request.getSpecialty());
        doctor.setExperience(request.getExperience());
        doctor.setQualification(request.getQualification());
        doctor.setDesignation(request.getDesignation());
        doctorRepository.save(doctor);

        return "Doctor profile updated.";
    }

    @Override
    public String toggleUserStatus(Long id, Status status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(status);
        userRepository.save(user);
        return "User status updated to " + status;
    }

}
