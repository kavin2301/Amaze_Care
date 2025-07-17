package com.hexaware.amazecare.controller;

import com.hexaware.amazecare.entity.Appointment;
import com.hexaware.amazecare.entity.AppointmentStatus;
import com.hexaware.amazecare.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<Appointment> bookAppointment(
            @RequestParam Long doctorId,
            @RequestParam String dateTime, 
            @RequestParam String notes) {

        String patientEmail = getLoggedInEmail();
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime);
        Appointment appointment = appointmentService.bookAppointment(doctorId, patientEmail, parsedDateTime, notes);

        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    // ✅ View appointments for logged-in patient
    @GetMapping("/patient")
    public ResponseEntity<List<Appointment>> getAppointmentsForPatient() {
        String patientEmail = getLoggedInEmail();
        List<Appointment> appointments = appointmentService.getAppointmentsForPatient(patientEmail);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<Appointment>> getAppointmentsForDoctor() {
        String doctorEmail = getLoggedInEmail();
        List<Appointment> appointments = appointmentService.getAppointmentsForDoctor(doctorEmail);
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status) {

        Appointment updated = appointmentService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id) {
        String patientEmail = getLoggedInEmail();
        boolean result = appointmentService.cancelAppointment(id, patientEmail);
        return result ?
                ResponseEntity.ok("Appointment cancelled") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to cancel appointment");
    }

    private String getLoggedInEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
