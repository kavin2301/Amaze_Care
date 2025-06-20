package com.hexaware.amazecare.controller;

import com.hexaware.amazecare.dto.MedicalRecordRequestDTO;
import com.hexaware.amazecare.entity.MedicalRecord;
import com.hexaware.amazecare.service.MedicalRecordService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/records")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @PostMapping("/add")
    public ResponseEntity<?> addMedicalRecord(@RequestBody MedicalRecordRequestDTO dto) {
        try {
            MedicalRecord record = medicalRecordService.saveRecord(
                    dto.getAppointmentId(),
                    dto.getDiagnosis(),
                    dto.getPhysicalExam(),
                    dto.getTestRecommended()
            );
            return ResponseEntity.ok(record);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Appointment not found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid input");
        }
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<MedicalRecord>> getRecordsByAppointment(@PathVariable int appointmentId) {
        List<MedicalRecord> list = medicalRecordService.getRecordsByAppointmentId(appointmentId);
        return ResponseEntity.ok(list);
    }
}