package com.hexaware.amazecare.repository;

import com.hexaware.amazecare.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {

    List<MedicalRecord> findByAppointmentAppointmentId(int appointmentId);
}
