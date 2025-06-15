package com.hexaware.amazecare.repository;

import com.hexaware.amazecare.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByDoctorDoctorId(int doctorId);
    List<Appointment> findByPatientPatientId(int patientId);
}
