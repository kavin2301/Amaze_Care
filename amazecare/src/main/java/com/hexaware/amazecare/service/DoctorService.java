package com.hexaware.amazecare.service;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private UserRepository userRepo;

    @Transactional
    public DoctorProfile createDoctorProfile(int userId, String specialty, String qualification, String designation, int experienceYears) {
        if (doctorRepo.existsByDoctorId(userId)) {
            throw new IllegalArgumentException("Doctor profile already exists");
        }
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

    public List<DoctorProfile> searchBySpecialty(String keyword) {
        return doctorRepo.findBySpecialtyContainingIgnoreCase(keyword);
    }

    public DoctorProfile getDoctorById(int id) {
        return doctorRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
    }

    public List<DoctorProfile> getAllDoctors() {
        return doctorRepo.findAll();
    }

    public void deleteDoctorProfile(int id) {
        if (!doctorRepo.existsById(id)) {
            throw new EntityNotFoundException("Doctor not found");
        }
        doctorRepo.deleteById(id);
    }
}
