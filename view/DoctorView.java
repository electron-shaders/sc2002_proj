package view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import controller.DoctorController;
import model.Appointment;
import model.Doctor;
import model.Medicine;
import model.Patient;
import model.Prescription;
import model.PrescriptionStatus;
import observer.Notification;
import store.AppointmentStore;

/**
 * The DoctorView class provides the user interface for doctors.
 * <p>
 * It allows the doctor to view notifications, view and update patient medical records, view and set personal schedule,
 * view upcoming appointments, accept or decline appointment requests, record appointment outcomes, and change password.
 * </p>
 */
public class DoctorView extends DashboardView {
    /**
     * The user associated with this DoctorView.
     */
    private Doctor user;

    /**
     * The list of notifications received by the doctor.
     */
    private List<Notification> notifications;

    /**
     * Constructor for the DoctorView class.
     * Initializes the notifications list and subscribes to the AppointmentStore.
     *
     * @param user The user associated with this DoctorView.
     */
    public DoctorView(Doctor user) {
        this.user = user;
        this.notifications = new ArrayList<Notification>();
        AppointmentStore.subscribe(this);
    }

    /**
     * Implements the ISubscriber interface.
     * Updates the list of notifications when receiving a new notification from subscribed publishers.
     *
     * @param notification The new notification to be added to the list.
     */
    public void update(Notification notification) {
        notifications.add(notification);
    }

    /**
     * Displays a menu with a set of actions that the patient can choose from, including:
     * <ol>
     *  <li>Show notifications</li>
     *  <li>View patient medical records</li>
     *  <li>Update patient medical records</li>
     *  <li>View personal schedule</li>
     *  <li>Set availability</li>
     *  <li>View upcoming appointments</li>
     *  <li>Accept an appointment request</li>
     *  <li>Decline an appointment request</li>
     *  <li>Record appointment outcome</li>
     *  <li>Change password</li>
     *  <li>Logout</li>
     * </ol>
     * The method runs in a while loop, i.e., it repeatedly displays the menu after the doctor completes an action until logout.
     */
    public void launch() {
        while(true) {
            System.out.println("======================================================================================================");
            System.out.println("|                                     HOSPITAL MANAGEMENT SYSTEM                                     |");
            System.out.println("|                                               DOCTOR DASHBOARD                                     |");
            System.out.println("======================================================================================================");
            System.out.println("Welcome, Dr. " + user.getName());
            if (notifications.size() > 0) {
                System.out.println("You have " + notifications.size() + " new notifications.");
            }
            System.out.println("What would you like to do?");
            System.out.println("1. Show notifications");
            System.out.println("2. View patient medical records");
            System.out.println("3. Update patient medical records");
            System.out.println("4. View your personal schedule");
            System.out.println("5. Set your availability");
            System.out.println("6. View your upcoming appointments");
            System.out.println("7. Accept a appointment request");
            System.out.println("8. Decline a appointment request");
            System.out.println("9. Record appointment outcome");
            System.out.println("10. Change password");
            System.out.println("11. Logout");
            System.out.println();

            int choice = getChoice(1, 11);
            switch (choice) {
                case 1:
                    List<String> notificationStrings = new ArrayList<String>();
                    for (Notification notification : notifications) {
                        notificationStrings.add(notification.toString());
                    }
                    showNotifications(notificationStrings);
                    notifications.clear();
                    break;
                case 2:
                    showPatientMedicalRecords();
                    break;
                case 3:
                    showUpdatePatientMedicalRecordForm();
                    break;
                case 4:
                    showSchedule();
                    break;
                case 5:
                    showSetAvailabilityForm();
                    break;
                case 6:
                    showUpcomingAppointments();
                    break;
                case 7:
                    showAcceptAppointmentForm();
                    break;
                case 8:
                    showDeclineAppointmentForm();
                    break;
                case 9:
                    showRecordAppointmentOutcomeForm();
                    break;
                case 10:
                    showChangePasswordForm(user);
                    break;
                case 11:
                    user.logout();
                    return;
            }
        }
    }

    /**
     * Displays the medical records of patients under the doctor's care,
     * i.e., those who have an appointment with the doctor.
     */
    public void showPatientMedicalRecords() {
        List<Patient> patients = DoctorController.getPatientsUnderCare(user.getUserId());
        if (patients.isEmpty()) {
            System.out.println("There are no patients under your care so far.");
            List<String> tips = new ArrayList<String>();
            tips.add("Try adding some available time slots to your schedule to allow patients to book appointments with you.");
            showUserTips(tips);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        showSuccess();
        System.out.println("The medical records of patients under your care are as follows:");
        for (Patient patient : patients) {
            System.out.println("Patient ID: " + patient.getUserId());
            System.out.println("Name: " + patient.getName());
            System.out.println("Date of Birth: " + sdf.format(patient.getDateOfBirth()));
            System.out.println("Gender: " + (patient.getIsMale() ? "Male" : "Female"));
            System.out.println("Email Address: " + patient.getEmail());
            System.out.println("Blood Type: " + patient.getBloodType());
            System.out.println("--------------------");
            System.out.println("Past diagnoses:");
            if (patient.getMedicalRecord().getDiagnoses().isEmpty()) {
                System.out.println("  (Empty)");
            }
            for (String diagnosis : patient.getMedicalRecord().getDiagnoses()) {
                System.out.println("- " + diagnosis);
            }
            System.out.println("--------------------");
            System.out.println("Past prescriptions:");
            if (patient.getMedicalRecord().getPrescriptions().isEmpty()) {
                System.out.println("  (Empty)");
            }
            for (String prescription : patient.getMedicalRecord().getPrescriptions()) {
                System.out.println("- " + prescription);
            }
            System.out.println("--------------------");
            System.out.println("Past treatment plans:");
            if (patient.getMedicalRecord().getTreatments().isEmpty()) {
                System.out.println("  (Empty)");
            }
            for (String treatment : patient.getMedicalRecord().getTreatments()) {
                System.out.println("- " + treatment);
            }
            System.out.println();
        }
    }

    /**
     * Displays a form to update a patient's medical record.
     * The doctor can choose to add a diagnosis, prescription, or treatment plan to the patient's medical record.
     */
    public void showUpdatePatientMedicalRecordForm() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the patient ID:");
        String patientId = sc.nextLine();
        System.out.println("What would you like to add?");
        System.out.println("1. Diagnosis");
        System.out.println("2. Prescription");
        System.out.println("3. Treatment plan");
        System.out.println("4. Quit");

        int choice = getChoice(1, 4);
        switch (choice) {
            case 1:
                System.out.println("Please enter the diagnosis:");
                String diagnosis = sc.nextLine();
                try {
                    DoctorController.addPatientDiagnosis(patientId, diagnosis);
                    showSuccess("Diagnosis added successfully.");
                } catch (Exception e) {
                    showError(e.getMessage());
                }
                break;
            case 2:
                System.out.println("Please enter the prescription:");
                String prescription = sc.nextLine();
                try {
                    DoctorController.addPatientPrescription(patientId, prescription);
                    showSuccess("Prescription added successfully.");
                } catch (Exception e) {
                    showError(e.getMessage());
                }
                break;
            case 3:
                System.out.println("Please enter the treatment plan:");
                String treatment = sc.nextLine();
                try {
                    DoctorController.addPatientTreatment(patientId, treatment);
                    showSuccess("Treatment plan added successfully.");
                } catch (Exception e) {
                    showError(e.getMessage());
                }
                break;
            case 4:
                return;
        }
    }

    /**
     * Displays the doctor's schedule.
     */
    public void showSchedule() {
        Map<Date, Boolean> schedule = user.getAvailability();
        if (schedule.isEmpty()) {
            System.out.println("You have not set your availability yet.");
            List<String> tips = new ArrayList<String>();
            tips.add("Try adding some available time slots to your schedule first.");
            showUserTips(tips);
            return;
        }

        showSuccess();
        System.out.println("Your personal schedule is as follows:");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Map.Entry<Date, Boolean> entry : schedule.entrySet()) {
            System.out.println(sdf.format(entry.getKey()) + ": " + (entry.getValue() ? "Available" : "Unavailable"));
        }
    }

    /**
     * Displays a form for the doctor to set their availability on a specific date
     * so that patients can schedule appointments with the doctor.
     */
    public void showSetAvailabilityForm() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the date (dd/MM/yyyy): ");
        String dateString = sc.nextLine();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            showError(e.getMessage());
            return;
        }
        if (user.getAvailability().containsKey(date)) {
            showError("This date is already in your schedule.");
            return;
        }

        user.addAvailability(date);
        showSuccess("Availability added successfully.");
    }

    /**
     * Displays the upcoming pending and accepted appointments for the doctor.
     */
    public void showUpcomingAppointments() {
        List<Appointment> appointments = DoctorController.getUpcomingAppointments(user.getUserId());
        if (appointments.isEmpty()) {
            System.out.println("You have no upcoming appointments.");
            List<String> tips = new ArrayList<String>();
            tips.add("Try adding some available time slots to your schedule to allow patients to book appointments with you.");
            showUserTips(tips);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        showSuccess();
        System.out.println("Your upcoming appointments are as follows:");
        for (Appointment appointment : appointments) {
            System.out.println("Appointment ID: " + appointment.getAppointmentId());
            System.out.println("Patient: " + appointment.getPatient().getName() + " (ID: " + appointment.getPatient().getUserId() + ")");
            System.out.println("Date: " + sdf.format(appointment.getDate()));
            System.out.println("Status: " + appointment.getStatus());
            System.out.println();
        }
    }

    /**
     * Displays a form to accept an appointment.
     */
    public void showAcceptAppointmentForm() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the appointment ID:");
        String appointmentId = sc.nextLine();
        try {
            DoctorController.acceptAppointment(user.getUserId(), appointmentId);
            showSuccess("Appointment accepted successfully.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /**
     * Displays a form to decline an appointment.
     */
    public void showDeclineAppointmentForm() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the appointment ID:");
        String appointmentId = sc.nextLine();
        try {
            DoctorController.declineAppointment(user.getUserId(), appointmentId);
            showSuccess("Appointment declined successfully.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /**
     * Displays a form to record the outcome of an appointment,
     * including the type of service provided, prescribed medicines, and consultation notes.
     */
    public void showRecordAppointmentOutcomeForm() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the appointment ID:");
        String appointmentId = sc.nextLine();
        System.out.println("Please enter the type of service provided:");
        String serviceType = sc.nextLine();
        List<Prescription> prescriptions = new ArrayList<Prescription>();
        while (true) {
            System.out.println("Please enter the prescribed medicine name (or type 'done' to finish):");
            String medicineName = sc.nextLine();
            if (medicineName.equals("done")) {
                break;
            }
            Medicine medicine = DoctorController.findMedicine(medicineName);
            if (medicine == null) {
                showError("Medicine not found.");
                continue;
            }
            prescriptions.add(new Prescription(medicine, PrescriptionStatus.PENDING));
        }
        System.out.println("Please enter the consultation notes (if any):");
        String notes = sc.nextLine();

        try {
            DoctorController.recordAppointmentOutcome(user.getUserId(), appointmentId, serviceType, prescriptions, notes);
            showSuccess("Appointment outcome recorded successfully.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }
}
