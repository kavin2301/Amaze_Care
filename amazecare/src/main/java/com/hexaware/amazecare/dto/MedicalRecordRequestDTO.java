package com.hexaware.amazecare.dto;

public class MedicalRecordRequestDTO {
    private int appointmentId;
    private String diagnosis;
    private String physicalExam;
    private String testRecommended;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPhysicalExam() {
        return physicalExam;
    }

    public void setPhysicalExam(String physicalExam) {
        this.physicalExam = physicalExam;
    }

    public String getTestRecommended() {
        return testRecommended;
    }

    public void setTestRecommended(String testRecommended) {
        this.testRecommended = testRecommended;
    }
}
