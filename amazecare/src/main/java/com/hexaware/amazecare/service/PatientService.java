package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.Patient;
import com.hexaware.amazecare.entity.User;

public interface PatientService {
    void registerPatient(User user, Patient patientDetails);
    Patient getPatientProfile(String email);
    Patient updatePatientProfile(String email, Patient updatedPatient);
}
