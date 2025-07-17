package com.hexaware.amazecare.controller;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @PostMapping
    public ResponseEntity<Consultation> addConsultation(
            @RequestParam Long appointmentId,
            @RequestBody Consultation consultation) {
        Consultation saved = consultationService.addConsultation(appointmentId, consultation);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<Consultation> getConsultationByAppointment(@PathVariable Long appointmentId) {
        Consultation consultation = consultationService.getConsultationByAppointmentId(appointmentId);
        return ResponseEntity.ok(consultation);
    }

    @PostMapping("/{consultationId}/prescriptions")
    public ResponseEntity<List<Prescription>> addPrescriptions(
            @PathVariable Long consultationId,
            @RequestBody List<Prescription> prescriptions) {
        List<Prescription> saved = consultationService.addPrescriptions(consultationId, prescriptions);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/{consultationId}/tests")
    public ResponseEntity<List<TestRecommendation>> addTestRecommendations(
            @PathVariable Long consultationId,
            @RequestBody List<TestRecommendation> tests) {
        List<TestRecommendation> saved = consultationService.addTestRecommendations(consultationId, tests);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{consultationId}/prescriptions")
    public ResponseEntity<List<Prescription>> getPrescriptions(@PathVariable Long consultationId) {
        List<Prescription> prescriptions = consultationService.getPrescriptions(consultationId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/{consultationId}/tests")
    public ResponseEntity<List<TestRecommendation>> getTestRecommendations(@PathVariable Long consultationId) {
        List<TestRecommendation> tests = consultationService.getTestRecommendations(consultationId);
        return ResponseEntity.ok(tests);
    }
    
    @PutMapping("/{consultationId}")
    public ResponseEntity<Consultation> updateConsultation(
            @PathVariable Long consultationId,
            @RequestBody Consultation updatedData) {
        Consultation updated = consultationService.updateConsultation(consultationId, updatedData);
        return ResponseEntity.ok(updated);
    }
}
