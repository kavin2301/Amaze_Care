package com.hexaware.amazecare;

import com.hexaware.amazecare.entity.*;
import com.hexaware.amazecare.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.Scanner;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    private UserService userService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private MedicalRecordService medicalRecordService;
    @Autowired
    private PrescriptionService prescriptionService;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== AMAZECARE MENU =====");
            System.out.println("1. Register User");
            System.out.println("2. Create Doctor Profile");
            System.out.println("3. Create Patient Profile");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    User user = new User();
                    System.out.print("Name: "); user.setName(sc.nextLine());
                    System.out.print("Email: "); user.setEmail(sc.nextLine());
                    System.out.print("Password: "); user.setPassword(sc.nextLine());
                    System.out.print("Mobile: "); user.setMobileNumber(sc.nextLine());
                    System.out.print("Role (DOCTOR/PATIENT/ADMIN): "); user.setRole(Role.valueOf(sc.nextLine().toUpperCase()));
                    User saved = userService.registerUser(user);
                    System.out.println("Registered User ID: " + saved.getUserId());
                }
                case 2 -> {
                    System.out.print("Enter existing User ID: ");
                    int userId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Specialty: "); String specialty = sc.nextLine();
                    System.out.print("Qualification: "); String qualification = sc.nextLine();
                    System.out.print("Designation: "); String designation = sc.nextLine();
                    System.out.print("Experience (years): "); int exp = sc.nextInt();
                    sc.nextLine();
                    try {
                        User user = userService.getUserById(userId);
                        doctorService.createDoctorProfile(user.getUserId(), specialty, qualification, designation, exp);
                        System.out.println("Doctor Profile created successfully");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 3 -> {
                    System.out.print("Enter existing User ID: ");
                    int userId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Date of Birth (YYYY-MM-DD): ");
                    LocalDate dob = LocalDate.parse(sc.nextLine());
                    System.out.print("Gender: ");
                    String gender = sc.nextLine();
                    try {
                        User user = userService.getUserById(userId);
                        patientService.createPatientProfile(user.getUserId(), dob, gender);
                        System.out.println("Patient Profile created successfully");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 4 -> {
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }
}