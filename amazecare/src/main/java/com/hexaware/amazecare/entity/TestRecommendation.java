package com.hexaware.amazecare.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "test_recommendations")
public class TestRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    private String testName;

    private String description;

    public TestRecommendation() {}

    public TestRecommendation(Consultation consultation, String testName, String description) {
        this.consultation = consultation;
        this.testName = testName;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Consultation getConsultation() { return consultation; }
    public void setConsultation(Consultation consultation) { this.consultation = consultation; }

    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
