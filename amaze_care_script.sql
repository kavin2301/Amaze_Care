DROP DATABASE amazecaredb;
CREATE DATABASE AmazeCareDB;
use amazecaredb;
show tables;

CREATE TABLE User (
  user_id INT PRIMARY KEY AUTO_INCREMENT,
  role ENUM('PATIENT', 'DOCTOR', 'ADMIN'),
  name VARCHAR(100),
  email VARCHAR(100) UNIQUE,
  password VARCHAR(255),
  mobile_number VARCHAR(15),
  created_at DATE
);

CREATE TABLE DoctorProfile (
  doctor_id INT PRIMARY KEY,
  specialty VARCHAR(100),
  qualification VARCHAR(100),
  designation VARCHAR(100),
  experience_years INT,
  FOREIGN KEY (doctor_id) REFERENCES User(user_id)
);

CREATE TABLE PatientProfile (
  patient_id INT PRIMARY KEY,
  date_of_birth DATE,
  gender VARCHAR(10),
  FOREIGN KEY (patient_id) REFERENCES User(user_id)
);

CREATE TABLE Appointment (
  appointment_id INT PRIMARY KEY AUTO_INCREMENT,
  patient_id INT,
  doctor_id INT,
  appointment_date DATE,
  status ENUM('Scheduled', 'Completed', 'Cancelled', 'Rejected'),
  symptoms TEXT,
  created_at DATETIME,
  FOREIGN KEY (patient_id) REFERENCES PatientProfile(patient_id),
  FOREIGN KEY (doctor_id) REFERENCES DoctorProfile(doctor_id)
);

CREATE TABLE MedicalRecord (
  record_id INT PRIMARY KEY AUTO_INCREMENT,
  appointment_id INT,
  diagnosis TEXT,
  physical_exam TEXT,
  test_recommended TEXT,
  created_at DATE,
  FOREIGN KEY (appointment_id) REFERENCES Appointment(appointment_id)
);


CREATE TABLE Prescription (
  prescription_id INT PRIMARY KEY AUTO_INCREMENT,
  appointment_id INT,
  medicine_name VARCHAR(100),
  dosage_pattern VARCHAR(20), 
  intake_time VARCHAR(10), 
  FOREIGN KEY (appointment_id) REFERENCES Appointment(appointment_id)
);


