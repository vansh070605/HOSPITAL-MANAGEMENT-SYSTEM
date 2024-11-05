# Hospital Management System

## Overview

The Hospital Management System is a Java Swing application designed to facilitate the management of patient and doctor information within a hospital. The system allows users to book appointments, view details of doctors and patients, and manage appointment records. The application connects to a MySQL database to store and retrieve information efficiently.

## Features

- User authentication with a username and password.
- Manage doctors: Add, view, and refresh doctor records.
- Manage patients: Add, view, and refresh patient records.
- Book appointments: Schedule appointments for patients with doctors.
- View appointment details: Display a list of all booked appointments.

## Technologies Used

- Java
- Java Swing for GUI
- MySQL for database management
- JDBC for database connectivity

## Database Schema

The database `hospital_management` includes the following tables:

### Doctors Table

```sql
CREATE TABLE doctors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL
);
