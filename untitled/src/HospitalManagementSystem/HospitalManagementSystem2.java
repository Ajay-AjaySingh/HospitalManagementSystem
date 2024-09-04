package HospitalManagementSystem;
import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem2 {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "root";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            User user = new User(connection);

            // Login
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (user.authenticate(username, password)) {
                String role = user.getRole(username);
                System.out.println("Login successful! Role: " + role);

                Patient patient = new Patient(connection, scanner);
                Doctor doctor = new Doctor(connection);

                while (true) {
                    System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                    System.out.println("1. Add Patient");
                    System.out.println("2. View Patients");
                    System.out.println("3. View Doctors");
                    System.out.println("4. Book Appointment");

                    // Admin and doctor only features
                    if (username.equals("admin") || username.equals("doctor1")) {
                        System.out.println("5. Add Medical Record");
                        System.out.println("6. View Medical Records");
                    }

                    // Admin only features
                    if (username.equals("admin")) {
                        System.out.println("7. Generate Bill");
                        System.out.println("8. View Bills");
                    }

                    System.out.println("9. Exit");
                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            // Add Patient
                            patient.addPatient();
                            System.out.println();
                            break;
                        case 2:
                            // View Patient
                            patient.viewPatients();
                            System.out.println();
                            break;
                        case 3:
                            // View Doctors
                            doctor.viewDoctors();
                            System.out.println();
                            break;
                        case 4:
                            // Book Appointment
                            bookAppointment(patient, doctor, connection, scanner);
                            System.out.println();
                            break;
                        case 5:
                            // Add Medical Record (admin and doctor only)
                            if (username.equals("admin") || username.equals("doctor1")) {
                                System.out.print("Enter Patient Id: ");
                                int patientIdForRecord = scanner.nextInt();
                                if (patient.getPatientById(patientIdForRecord)) {
                                    patient.addMedicalRecord(patientIdForRecord);
                                } else {
                                    System.out.println("Patient not found!");
                                }
                                System.out.println();
                            } else {
                                System.out.println("Unauthorized access!");
                            }
                            break;
                        case 6:
                            // View Medical Records (admin and doctor only)
                            if (username.equals("admin") || username.equals("doctor1")) {
                                System.out.print("Enter Patient Id: ");
                                int patientIdForViewingRecords = scanner.nextInt();
                                if (patient.getPatientById(patientIdForViewingRecords)) {
                                    patient.viewMedicalRecords(patientIdForViewingRecords);
                                } else {
                                    System.out.println("Patient not found!");
                                }
                                System.out.println();
                            } else {
                                System.out.println("Unauthorized access!");
                            }
                            break;
                        case 7:
                            // Generate Bill (admin only)
                            if (username.equals("admin")) {
                                System.out.print("Enter Patient Id: ");
                                int patientIdForBill = scanner.nextInt();
                                if (patient.getPatientById(patientIdForBill)) {
                                    patient.generateBill(patientIdForBill);
                                } else {
                                    System.out.println("Patient not found!");
                                }
                                System.out.println();
                            } else {
                                System.out.println("Unauthorized access!");
                            }
                            break;
                        case 8:
                            // View Bills (admin only)
                            if (username.equals("admin")) {
                                System.out.print("Enter Patient Id: ");
                                int patientIdForViewingBills = scanner.nextInt();
                                if (patient.getPatientById(patientIdForViewingBills)) {
                                    patient.viewBills(patientIdForViewingBills);
                                } else {
                                    System.out.println("Patient not found!");
                                }
                                System.out.println();
                            } else {
                                System.out.println("Unauthorized access!");
                            }
                            break;
                        case 9:
                            // Exit
                            System.out.println("Exiting...");
                            return;
                        default:
                            System.out.println("Invalid choice! Please try again.");
                    }
                }
            } else {
                System.out.println("Invalid username or password!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
        System.out.print("Enter Patient Id: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor Id: ");
        int doctorId = scanner.nextInt();
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();
        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
            if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {
                String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Appointment Booked!");
                    } else {
                        System.out.println("Failed to Book Appointment!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Doctor not available on this date!!");
            }
        } else {
            System.out.println("Either doctor or patient doesn't exist!!!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}


