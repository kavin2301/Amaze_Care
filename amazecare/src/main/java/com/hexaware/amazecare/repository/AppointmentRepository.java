package com.hexaware.amazecare.repository;

import com.hexaware.amazecare.entity.Appointment;
import com.hexaware.amazecare.entity.Status;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> findByDoctorDoctorId(int doctorId);

    List<Appointment> findByPatientPatientId(int patientId);

    List<Appointment> findByStatus(Status status);
}
