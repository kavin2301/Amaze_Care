package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.DoctorProfile;
import com.hexaware.amazecare.entity.User;
import com.hexaware.amazecare.repository.DoctorRepository;
import com.hexaware.amazecare.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private UserRepository userRepo;

    @Transactional
    public DoctorProfile createDoctorProfile(int userId, String specialty, String qualification, String designation, int experienceYears) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        DoctorProfile profile = new DoctorProfile();
        profile.setUser(user);
        profile.setSpecialty(specialty);
        profile.setQualification(qualification);
        profile.setDesignation(designation);
        profile.setExperienceYears(experienceYears);

        return doctorRepo.save(profile); 
    }
}