package com.hexaware.amazecare.repository;

import com.hexaware.amazecare.entity.Consultation;
import com.hexaware.amazecare.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    Optional<Consultation> findByAppointment(Appointment appointment);
    
}
