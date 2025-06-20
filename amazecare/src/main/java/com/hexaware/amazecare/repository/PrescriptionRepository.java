package com.hexaware.amazecare.repository;

import com.hexaware.amazecare.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {

    List<Prescription> findByAppointmentAppointmentId(int appointmentId);
}
