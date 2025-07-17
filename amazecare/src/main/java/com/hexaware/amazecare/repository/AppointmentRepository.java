package com.hexaware.amazecare.repository;

import com.hexaware.amazecare.entity.Appointment;
import com.hexaware.amazecare.entity.Doctor;
import com.hexaware.amazecare.entity.Patient;
import com.hexaware.amazecare.entity.AppointmentStatus;
import com.hexaware.amazecare.entity.Consultation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	List<Appointment> findByDoctor(Doctor doctor);
	List<Appointment> findByPatient(Patient patient);
	List<Appointment> findByAppointmentDateTimeBetween(LocalDateTime start, LocalDateTime end);
	List<Appointment> findByStatus(AppointmentStatus status);
}
