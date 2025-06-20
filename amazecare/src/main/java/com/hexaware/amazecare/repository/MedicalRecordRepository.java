package com.hexaware.amazecare.repository;

import com.hexaware.amazecare.entity.MedicalRecord;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {

    List<MedicalRecord> findByAppointmentAppointmentId(int appointmentId);
}
