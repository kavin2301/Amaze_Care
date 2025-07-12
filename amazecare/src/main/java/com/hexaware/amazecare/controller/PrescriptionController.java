package com.hexaware.amazecare.controller;

import com.hexaware.amazecare.dto.PrescriptionRequestDTO;
import com.hexaware.amazecare.entity.Prescription;
import com.hexaware.amazecare.service.PrescriptionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping("/add")
    public ResponseEntity<?> addPrescription(@RequestBody PrescriptionRequestDTO dto) {
        try {
            Prescription p = prescriptionService.addPrescription(
                    dto.getAppointmentId(),
                    dto.getMedicineName(),
                    dto.getDosagePattern(),
                    dto.getIntakeTime()
            );
            return ResponseEntity.ok(p);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Appointment not found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        }
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByAppointment(@PathVariable int appointmentId) {
        List<Prescription> list = prescriptionService.getPrescriptionsByAppointmentId(appointmentId);
        return ResponseEntity.ok(list);
    }
}
