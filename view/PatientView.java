package view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import controller.PatientController;
import model.Appointment;
import model.AppointmentOutcomeRecord;
import model.AppointmentStatus;
import model.Doctor;
import model.Patient;
import model.Prescription;
import observer.Notification;

public class PatientView extends DashboardView {
    private Patient user;
    private List<Notification> notifications;

    public PatientView(Patient user) {
        this.user = user;
        this.notifications = new ArrayList<Notification>();
    }

    public void update(Notification notification) {
        notifications.add(notification);
    }

    public void launch() {
        while(true) {
            System.out.println("======================================================================================================");
            System.out.println("|                                     HOSPITAL MANAGEMENT SYSTEM                                     |");
            System.out.println("|                                              PATIENT DASHBOARD                                     |");
            System.out.println("======================================================================================================");
            System.out.println("Welcome, " + user.getName());
            System.out.println("What would you like to do?");
            System.out.println("1. Show notifications");
            System.out.println("2. View my medical records");
            System.out.println("3. Update my personal information");
            System.out.println("4. Search for a doctor");
            System.out.println("5. Schedule an appointment");
            System.out.println("6. Reschedule an appointment");
            System.out.println("7. Cancel an appointment");
            System.out.println("8. View my scheduled appointments");
            System.out.println("9. View past appointment outcome records");
            System.out.println("10. Provide rating for a past appointment");
            System.out.println("11. Print bill for an appointment");
            System.out.println("12. Change password");
            System.out.println("13. Log out");
            System.out.println();

            int choice = getChoice(1, 13);
            switch (choice) {
                case 1:
                    List<String> notificationStrings = new ArrayList<String>();
                    for (Notification notification : notifications) {
                        if (notification.getObjectName() != null && notification.getObjectName().equals("Patient") && notification.getObjectId().equals(user.getUserId())) {
                            notificationStrings.add(notification.toString());
                        } else {
                            notificationStrings.add(notification.toString());
                        }
                    }
                    showNotifications(notificationStrings);
                    notifications.clear();
                    break;
                case 2:
                    showMedicalRecord();
                    break;
                case 3:
                    showUpdatePersonalInfoForm();
                    break;
                case 4:
                    showDoctorSearchForm();
                    break;
                case 5:
                    showScheduleAppointmentForm();
                    break;
                case 6:
                    showRescheduleAppointmentForm();
                    break;
                case 7:
                    showCancelAppointmentForm();
                    break;
                case 8:
                    showScheduledAppointments();
                    break;
                case 9:
                    showPastAppointmentOutcomeRecords();
                    break;
                case 10:
                    showProvideRatingForm();
                    break;
                case 11:
                    showPrintBillForm();
                    break;
                case 12:
                    showChangePasswordForm(user);
                    break;
                case 13:
                    user.logout();
                    return;
            }
        }
    }

    public void showMedicalRecord() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        showSuccess();
        System.out.println("Your medical record found in the HMS:");
        System.out.println("Patient ID: " + user.getUserId());
        System.out.println("Name: " + user.getName());
        System.out.println("Date of birth: " + sdf.format(user.getDateOfBirth()));
        System.out.println("Gender: " + (user.getIsMale() ? "Male" : "Female"));
        System.out.println("Email: " + user.getEmail());
        System.out.println("Blood type: " + user.getBloodType());
        System.out.println("--------------------");
        System.out.println("Past diagnoses:");
        if (user.getMedicalRecord().getDiagnoses().isEmpty()) {
            System.out.println("  (Empty)");
        }
        for (String diagnosis : user.getMedicalRecord().getDiagnoses()) {
            System.out.println("- " + diagnosis);
        }
        System.out.println("--------------------");
        System.out.println("Past prescriptions:");
        if (user.getMedicalRecord().getPrescriptions().isEmpty()) {
            System.out.println("  (Empty)");
        }
        for (String prescription : user.getMedicalRecord().getPrescriptions()) {
            System.out.println("- " + prescription);
        }
        System.out.println("--------------------");
        System.out.println("Past treatment plans:");
        if (user.getMedicalRecord().getTreatments().isEmpty()) {
            System.out.println("  (Empty)");
        }
        for (String treatment : user.getMedicalRecord().getTreatments()) {
            System.out.println("- " + treatment);
        }
    }

    public void showUpdatePersonalInfoForm() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Which personal information would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. Date of birth");
        System.out.println("3. Email address");
        System.out.println("4. Quit");

        int choice = getChoice(1, 4);
        switch (choice) {
            case 1:
                System.out.print("Enter your new name: ");
                String newName = sc.nextLine();
                user.setName(newName);
                showSuccess("Name updated successfully.");
                break;
            case 2:
                System.out.print("Enter your new date of birth (dd/MM/yyyy): ");
                String newDateOfBirth = sc.nextLine();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dateOfBirth;
                try {
                    dateOfBirth = sdf.parse(newDateOfBirth);
                } catch (ParseException e) {
                    showError(e.getMessage());
                    return;
                }
                user.setDateOfBirth(dateOfBirth);
                showSuccess("Date of birth updated successfully.");
                break;
            case 3:
                System.out.print("Enter your new email address: ");
                String newEmail = sc.nextLine();
                user.updatePersonalInfo(newEmail);
                showSuccess("Email address updated successfully.");
                break;
            case 4:
                return;
        }
    }

    public void showDoctorSearchForm() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the specialty of the doctor you are looking for: ");
        String specialty = sc.nextLine();
        List<Doctor> doctors = PatientController.searchDoctor(specialty);
        if (doctors.isEmpty()) {
            System.out.println("No doctors found with the specified specialty.");
            List<String> tips = new ArrayList<String>();
            tips.add("Try searching for a different specialty.");
            tips.add("Try using a more general term for the specialty.");
            tips.add("Try leaving the specialty field blank to see all doctors.");
            showUserTips(tips);
            return;
        }

        showSuccess();
        System.out.println("Found " + doctors.size() + " doctors:");
        for (int i = 0; i < doctors.size(); i++) {
            Doctor doctor = doctors.get(i);
            System.out.println(doctor.getUserId() + ": " + doctor.getName() + " (" + doctor.getSpecialty() + ")" + ", Rating: " + String.format("%.2f", doctor.getRating()) + " (" + doctor.getRatingCount() + ")");
            System.out.println("   " + doctor.getName() + "'s avaialability:");
            showAppointmentSlots(doctor.getUserId());
            System.out.println();
        }
    }

    public void showAppointmentSlots(String doctorId) {
        List<Date> dates;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dates = PatientController.getAppointmentSlots(doctorId);
        } catch (Exception e) {
            showError(e.getMessage());
            return;
        }
        for (Date date : dates) {
            System.out.println("   - " + sdf.format(date));
        }
    }

    public void showScheduleAppointmentForm() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the ID of the doctor you would like to schedule an appointment with: ");
        String doctorId = sc.nextLine();
        System.out.print("Enter the date of the appointment (dd/MM/yyyy): ");
        String dateString = sc.nextLine();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = sdf.parse(dateString);
            Appointment appointment = PatientController.scheduleAppointment(user.getUserId(), doctorId, date);
            appointment.subscribe(this);
            showSuccess("Appointment scheduled successfully.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    public void showRescheduleAppointmentForm() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the ID of the appointment you would like to reschedule: ");
        String appointmentId = sc.nextLine();
        System.out.print("Enter the new date of the appointment (dd/MM/yyyy): ");
        String dateString = sc.nextLine();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = sdf.parse(dateString);
            Appointment appointment = PatientController.rescheduleAppointment(user.getUserId(), appointmentId, date);
            appointment.subscribe(this);
            showSuccess("Appointment rescheduled successfully.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    public void showCancelAppointmentForm() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the ID of the appointment you would like to cancel: ");
        String appointmentId = sc.nextLine();
        try {
            PatientController.cancelAppointment(user.getUserId(), appointmentId);
            showSuccess("Appointment cancelled successfully.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    public void showScheduledAppointments() {
        List<Appointment> appointments;
        try {
            appointments = PatientController.getScheduledAppointments(user.getUserId());
        } catch (Exception e) {
            showError(e.getMessage());
            return;
        }

        if (appointments.isEmpty()) {
            System.out.println("You have no scheduled appointments.");
            List<String> tips = new ArrayList<String>();
            tips.add("Try searching for a doctor first.");
            tips.add("Try scheduling an appointment with a doctor.");
            showUserTips(tips);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        showSuccess();
        System.out.println("Your scheduled appointments:");
        for (Appointment appointment : appointments) {
            System.out.println("Appointment ID: " + appointment.getAppointmentId());
            System.out.println("Doctor: " + appointment.getDoctor().getName());
            System.out.println("Date: " + sdf.format(appointment.getDate()));
            System.out.println("Status: " + appointment.getStatus());
            System.out.println();
        }
    }

    public void showPastAppointmentOutcomeRecords() {
        List<Appointment> appointments;
        try {
            appointments = PatientController.getPastAppointments(user.getUserId());
        } catch (Exception e) {
            showError(e.getMessage());
            return;
        }

        if (appointments.isEmpty()) {
            System.out.println("You have no past appointment outcome records.");
            List<String> tips = new ArrayList<String>();
            tips.add("Try scheduling an appointment with a doctor.");
            showUserTips(tips);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        showSuccess();
        System.out.println("Your past appointment outcome records:");
        for (Appointment appointment : appointments) {
            System.out.println("Appointment ID: " + appointment.getAppointmentId());
            System.out.println("Doctor: " + appointment.getDoctor().getName());
            System.out.println("Date: " + sdf.format(appointment.getDate()));
            System.out.println("Status: " + appointment.getStatus());
            if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
                continue;
            }

            AppointmentOutcomeRecord outcomeRecord;
            try {
                outcomeRecord = PatientController.getAppointmentOutcomeRecord(user.getUserId(), appointment.getAppointmentId());
            } catch (Exception e) {
                showError(e.getMessage());
                continue;
            }

            System.out.println("Type of service: " + outcomeRecord.getServiceType());
            System.out.println("Prescribed medications: ");
            List<Prescription> prescriptions = outcomeRecord.getPrescriptions();
            if (prescriptions.isEmpty()) {
                System.out.println("    (None)");
            }
            for (Prescription prescription : prescriptions) {
                System.out.println("   - " + prescription.getMedicine().getName() + " (" + prescription.getStatus() + ")");
            }
            System.out.println("Consultation notes: " + outcomeRecord.getNotes());
        }
    }

    public void showProvideRatingForm() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the ID of the appointment to rate: ");
        String appointmentId = sc.nextLine();
        System.out.print("Enter your rating (1-5): ");
        int rating = sc.nextInt();
        try {
            PatientController.provideRating(user.getUserId(), appointmentId, rating);
            showSuccess("Rating provided successfully.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    public void showPrintBillForm() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the ID of the appointment to print bill: ");
        String appointmentId = sc.nextLine();
        try {
            String billString = PatientController.printBill(user.getUserId(), appointmentId);
            showSuccess();
            System.out.println("Bill for appointment " + appointmentId + ":");
            System.out.println(billString);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }
}
