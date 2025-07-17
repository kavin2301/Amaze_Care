package com.hexaware.amazecare.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    private String medicineName;

    private String dosage; // e.g., 500mg

    private String timing; // e.g., 0-0-1

    private String instruction; // e.g., AF (after food)

    public Prescription() {}

    public Prescription(Consultation consultation, String medicineName, String dosage, String timing, String instruction) {
        this.consultation = consultation;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.timing = timing;
        this.instruction = instruction;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Consultation getConsultation() { return consultation; }
    public void setConsultation(Consultation consultation) { this.consultation = consultation; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getTiming() { return timing; }
    public void setTiming(String timing) { this.timing = timing; }

    public String getInstruction() { return instruction; }
    public void setInstruction(String instruction) { this.instruction = instruction; }
}
