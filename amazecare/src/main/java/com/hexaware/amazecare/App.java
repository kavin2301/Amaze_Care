package com.hexaware.amazecare;

import com.hexaware.amazecare.entity.Role;
import com.hexaware.amazecare.entity.User;
import com.hexaware.amazecare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = "com.hexaware.amazecare")
public class App implements CommandLineRunner {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
   
    @Override
    public void run(String... args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== AMAZECARE - Console App =====");

        while (true) {
            System.out.println("\n1. Register User");
            System.out.println("2. Find User by Email");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1 -> {
                    User user = new User();
                    System.out.print("Enter Name: ");
                    user.setName(sc.nextLine());

                    System.out.print("Enter Email: ");
                    user.setEmail(sc.nextLine());

                    System.out.print("Enter Password: ");
                    user.setPassword(sc.nextLine());

                    System.out.print("Enter Mobile Number: ");
                    user.setMobileNumber(sc.nextLine());

                    System.out.print("Enter Role (PATIENT / DOCTOR / ADMIN): ");
                    user.setRole(Role.valueOf(sc.nextLine().toUpperCase()));

                    try {
                        User saved = userService.registerUser(user);
                        System.out.println("Registered: " + saved.getName() + " (ID: " + saved.getUserId() + ")");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 2 -> {
                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();
                    try {
                        User found = userService.findUserByEmail(email);
                        System.out.println("User Found: " + found.getName() + " [" + found.getRole() + "]");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 3 -> {
                    System.out.println("Exiting AmazeCare Console App...");
                    sc.close();
                    return;
                }

                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}