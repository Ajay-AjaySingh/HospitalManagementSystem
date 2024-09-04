package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient(){
        System.out.print("Enter Patient Name: ");
        String name = scanner.next();
        System.out.print("Enter Patient Age: ");
        int age = scanner.nextInt();
        System.out.print("Enter Patient Gender: ");
        String gender = scanner.next();

        try{
            String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Patient Added Successfully!!");
            }else{
                System.out.println("Failed to add Patient!!");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatients(){
        String query = "SELECT * FROM patients";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+------------+--------------------+----------+------------+");
            System.out.println("| Patient Id | Name               | Age      | Gender     |");
            System.out.println("+------------+--------------------+----------+------------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("| %-10s | %-18s | %-8s | %-10s |\n", id, name, age, gender);
                System.out.println("+------------+--------------------+----------+------------+");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id){
        String query = "SELECT * FROM patients WHERE id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            } else {
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    // Method to add a medical record
    public void addMedicalRecord(int patientId) {
        System.out.print("Enter Diagnosis: ");
        String diagnosis = scanner.next();
        System.out.print("Enter Treatment: ");
        String treatment = scanner.next();
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = scanner.next();

        String query = "INSERT INTO medical_records(patient_id, diagnosis, treatment, date) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, patientId);
            preparedStatement.setString(2, diagnosis);
            preparedStatement.setString(3, treatment);
            preparedStatement.setString(4, date);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Medical Record Added Successfully!!");
            } else {
                System.out.println("Failed to Add Medical Record!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view medical records of a patient
    public void viewMedicalRecords(int patientId) {
        String query = "SELECT * FROM medical_records WHERE patient_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, patientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Medical Records: ");
            System.out.println("+------------+--------------------+--------------------+------------+");
            System.out.println("| Record Id  | Diagnosis          | Treatment          | Date       |");
            System.out.println("+------------+--------------------+--------------------+------------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String diagnosis = resultSet.getString("diagnosis");
                String treatment = resultSet.getString("treatment");
                String date = resultSet.getString("date");
                System.out.printf("| %-10s | %-18s | %-18s | %-10s |\n", id, diagnosis, treatment, date);
                System.out.println("+------------+--------------------+--------------------+------------+");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to generate a bill for a patient
    public void generateBill(int patientId) {
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = scanner.next();

        String query = "INSERT INTO bills(patient_id, amount, date) VALUES(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, patientId);
            preparedStatement.setDouble(2, amount);
            preparedStatement.setString(3, date);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Bill Generated Successfully!!");
            } else {
                System.out.println("Failed to Generate Bill!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view bills of a patient
    public void viewBills(int patientId) {
        String query = "SELECT * FROM bills WHERE patient_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, patientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Bills: ");
            System.out.println("+------------+----------+------------+");
            System.out.println("| Bill Id    | Amount   | Date       |");
            System.out.println("+------------+----------+------------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                double amount = resultSet.getDouble("amount");
                String date = resultSet.getString("date");
                System.out.printf("| %-10s | %-8.2f | %-10s |\n", id, amount, date);
                System.out.println("+------------+----------+------------+");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
