package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.DoctorProfile;
import com.hexaware.amazecare.entity.User;
import com.hexaware.amazecare.repository.DoctorRepository;
import com.hexaware.amazecare.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepo;
    @Autowired
    private UserRepository userRepo;

    public DoctorProfile createDoctorProfile(int userId, String specialty, String qualification, String designation, int experienceYears) {
        User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        DoctorProfile doctor = new DoctorProfile();
        doctor.setUser(user);
        doctor.setSpecialty(specialty);
        doctor.setQualification(qualification);
        doctor.setDesignation(designation);
        doctor.setExperienceYears(experienceYears);
        return doctorRepo.save(doctor);
    }

    public List<DoctorProfile> getAllDoctors() {
        return doctorRepo.findAll();
    }
}
