package com.hexaware.amazecare.controller;

import com.hexaware.amazecare.dto.AppointmentRequestDTO;
import com.hexaware.amazecare.entity.Appointment;
import com.hexaware.amazecare.entity.Status;
import com.hexaware.amazecare.service.AppointmentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

   
    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequestDTO dto) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dto.getAppointmentDateTime());
            Appointment appointment = appointmentService.bookAppointment(
                    dto.getPatientId(),
                    dto.getDoctorId(),
                    dateTime,
                    dto.getSymptoms()
            );
            return ResponseEntity.ok(appointment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid input or date format (expected yyyy-MM-ddTHH:mm)");
        }
    }

    
    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelAppointment(@PathVariable int id) {
        try {
            appointmentService.cancelAppointment(id);
            return ResponseEntity.ok("Appointment cancelled successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

   
    @GetMapping("/patient/{id}")
    public ResponseEntity<List<Appointment>> getByPatient(@PathVariable int id) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(id));
    }

    
    @GetMapping("/doctor/{id}")
    public ResponseEntity<List<Appointment>> getByDoctor(@PathVariable int id) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorId(id));
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(appointmentService.getAppointmentById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Appointment not found");
        }
    }

   
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Appointment>> getByStatus(@PathVariable Status status) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(status));
    }
}
