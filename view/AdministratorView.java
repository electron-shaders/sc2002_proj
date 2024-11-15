package view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.AdministratorController;
import model.Appointment;
import model.AppointmentOutcomeRecord;
import model.AppointmentStatus;
import model.Doctor;
import model.Medicine;
import model.Prescription;
import model.User;
import model.UserRole;
import observer.Notification;
import store.AppointmentStore;

public class AdministratorView extends DashboardView {
    private User user;
    private List<Notification> notifications;

    public AdministratorView(User user) {
        this.user = user;
        this.notifications = new ArrayList<Notification>();
        AppointmentStore.subscribe(this);
    }

    public void update(Notification notification) {
        notifications.add(notification);
    }

    public void launch() {
        while(true) {
            System.out.println("======================================================================================================");
            System.out.println("|                                     HOSPITAL MANAGEMENT SYSTEM                                     |");
            System.out.println("|                                        ADMINISTRATOR DASHBOARD                                     |");
            System.out.println("======================================================================================================");
            System.out.println("Welcome, " + user.getName());
            if (notifications.size() > 0) {
                System.out.println("You have " + notifications.size() + " new notifications.");
            }
            System.out.println("What would you like to do?");
            System.out.println("1. Show notifications");
            System.out.println("2. View staff");
            System.out.println("3. Manage staff");
            System.out.println("4. View appointment details");
            System.out.println("5. View medication inventory");
            System.out.println("6. Manage medication inventory");
            System.out.println("7. Approve replenishment request");
            System.out.println("8. Change password");
            System.out.println("9. Logout");

            int choice = getChoice(1, 9);
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
                    showHospitalStaff();
                    break;
                case 3:
                    showManageHospitalStaffForm();
                    break;
                case 4:
                    showAppointmentDetails();
                    break;
                case 5:
                    showMedicationInventory();
                    break;
                case 6:
                    showManageMedicationInventoryForm();
                    break;
                case 7:
                    showApproveReplenishmentRequestForm();
                    break;
                case 8:
                    showChangePasswordForm(user);
                    break;
                case 9:
                    user.logout();
                    return;
            }
        }
    }

    public void showHospitalStaff() {
        System.out.println("How would you like to filter the staff?");
        System.out.println("1. Filter by role");
        System.out.println("2. Filter by age");
        System.out.println("3. Filter by gender");
        System.out.println("4. Filter by name");
        System.out.println("5. Quit");

        int choice = getChoice(1, 5);
        switch (choice) {
            case 1:
                showHospitalStaffByRole();
                break;
            case 2:
                showHospitalStaffByAge();
                break;
            case 3:
                showHospitalStaffByGender();
                break;
            case 4:
                showHospitalStaffByName();
                break;
            case 5:
                return;
        }
    }

    public void showHospitalStaffByRole() {
        System.out.println("What role would you like to view?");
        System.out.println("1. Doctor");
        System.out.println("2. Pharmacist");
        System.out.println("3. Quit");

        int choice = getChoice(1, 3);
        List<User> staffs;
        try {
            switch (choice) {
                case 1:
                    staffs = AdministratorController.searchStaff(UserRole.DOCTOR);
                    break;
                case 2:
                    staffs = AdministratorController.searchStaff(UserRole.PHARMACIST);
                    break;
                default:
                    return;
            }
        } catch (Exception e) {
            showError(e.getMessage());
            return;
        }

        if (staffs.isEmpty()) {
            System.out.println("No staff found.");
            List<String> tips = new ArrayList<String>();
            tips.add("Try adding a new staff member first.");
            tips.add("Try filtering staff by another role.");
            tips.add("Try filtering staff by another criteria.");
            showUserTips(tips);
            return;
        }

        showSuccess();
        System.out.println("The following staff members were found:");
        for (User staff : staffs) {
            System.out.println("Staff ID: " + staff.getUserId());
            System.out.println("Role: " + staff.getRole());
            System.out.println("Name: " + staff.getName());
            System.out.println("Gender: " + (staff.getIsMale() ? "Male" : "Female"));
            System.out.println("Age: " + staff.getAge());
            System.out.println("Email: " + staff.getEmail());
            if (staff.getRole() == UserRole.DOCTOR) {
                System.out.println("Specialty: " + ((Doctor) staff).getSpecialty());
            }
            System.out.println();
        }
    }

    public void showHospitalStaffByAge() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the age: ");
        int age = sc.nextInt();

        List<User> staffs = AdministratorController.searchStaff(age);

        if (staffs.isEmpty()) {
            System.out.println("No staff found.");
            List<String> tips = new ArrayList<String>();
            tips.add("Try adding a new staff member first.");
            tips.add("Try filtering staff by another age.");
            tips.add("Try filtering staff by another criteria.");
            showUserTips(tips);
            return;
        }

        showSuccess();
        System.out.println("The following staff members were found:");
        for (User staff : staffs) {
            System.out.println("Staff ID: " + staff.getUserId());
            System.out.println("Role: " + staff.getRole());
            System.out.println("Name: " + staff.getName());
            System.out.println("Gender: " + (staff.getIsMale() ? "Male" : "Female"));
            System.out.println("Age: " + staff.getAge());
            System.out.println("Email: " + staff.getEmail());
            if (staff.getRole() == UserRole.DOCTOR) {
                System.out.println("Specialty: " + ((Doctor) staff).getSpecialty());
            }
            System.out.println();
        }
    }

    public void showHospitalStaffByGender() {
        System.out.println("What gender would you like to view?");
        System.out.println("1. Male");
        System.out.println("2. Female");
        System.out.println("3. Quit");

        int choice = getChoice(1, 3);
        List<User> staffs;
        switch (choice) {
            case 1:
                staffs = AdministratorController.searchStaff(true);
                break;
            case 2:
                staffs = AdministratorController.searchStaff(false);
                break;
            default:
                return;
        }

        if (staffs.isEmpty()) {
            System.out.println("No staff found.");
            List<String> tips = new ArrayList<String>();
            tips.add("Try adding a new staff member first.");
            tips.add("Try filtering staff by another gender.");
            tips.add("Try filtering staff by another criteria.");
            showUserTips(tips);
            return;
        }

        showSuccess();
        System.out.println("The following staff members were found:");
        for (User staff : staffs) {
            System.out.println("Staff ID: " + staff.getUserId());
            System.out.println("Role: " + staff.getRole());
            System.out.println("Name: " + staff.getName());
            System.out.println("Gender: " + (staff.getIsMale() ? "Male" : "Female"));
            System.out.println("Age: " + staff.getAge());
            System.out.println("Email: " + staff.getEmail());
            if (staff.getRole() == UserRole.DOCTOR) {
                System.out.println("Specialty: " + ((Doctor) staff).getSpecialty());
            }
            System.out.println();
        }
    }

    public void showHospitalStaffByName() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the name: ");
        String name = sc.nextLine();

        List<User> staffs = AdministratorController.searchStaff(name);

        if (staffs.isEmpty()) {
            System.out.println("No staff found.");
            List<String> tips = new ArrayList<String>();
            tips.add("Try adding a new staff member first.");
            tips.add("Try filtering staff by another name.");
            tips.add("Try filtering staff by another criteria.");
            showUserTips(tips);
            return;
        }

        showSuccess();
        System.out.println("The following staff members were found:");
        for (User staff : staffs) {
            System.out.println("Staff ID: " + staff.getUserId());
            System.out.println("Role: " + staff.getRole());
            System.out.println("Name: " + staff.getName());
            System.out.println("Gender: " + (staff.getIsMale() ? "Male" : "Female"));
            System.out.println("Age: " + staff.getAge());
            System.out.println("Email: " + staff.getEmail());
            if (staff.getRole() == UserRole.DOCTOR) {
                System.out.println("Specialty: " + ((Doctor) staff).getSpecialty());
            }
            System.out.println();
        }
    }

    public void showManageHospitalStaffForm() {
        System.out.println("What would you like to do?");
        System.out.println("1. Add a new staff member");
        System.out.println("2. Update a staff member");
        System.out.println("3. Remove a staff member");
        System.out.println("4. Quit");

        int choice = getChoice(1, 4);
        switch (choice) {
            case 1:
                showAddHospitalStaffForm();
                break;
            case 2:
                showUpdateHospitalStaffForm();
                break;
            case 3:
                showRemoveHospitalStaffForm();
                break;
            case 4:
                return;
        }
    }

    public void showAddHospitalStaffForm() {
        System.out.println("What role would you like to add?");
        System.out.println("1. Doctor");
        System.out.println("2. Pharmacist");
        System.out.println("3. Quit");

        int choice = getChoice(1, 3);
        UserRole role;
        switch (choice) {
            case 1:
                role = UserRole.DOCTOR;
                break;
            case 2:
                role = UserRole.PHARMACIST;
                break;
            default:
                return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the following details:");
        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.println("Gender:");
        System.out.println("1. Male");
        System.out.println("2. Female");
        boolean isMale = getChoice(1, 2) == 1;

        System.out.print("Age: ");
        int age = sc.nextInt();

        System.out.print("Email: ");
        String email = sc.next();

        if (role == UserRole.PHARMACIST) {
            User user = new User(null, "password", role, name, isMale, age, email);
            try {
                AdministratorController.addStaff(user);
            } catch (Exception e) {
                showError(e.getMessage());
            }
            showSuccess("Staff added successfully.");
            return;
        }

        System.out.print("Specialty: ");
        String specialty = sc.nextLine();
        User user = new Doctor(null, "password", role, name, isMale, age, email, specialty, 0, 0);
        try {
            AdministratorController.addStaff(user);
        } catch (Exception e) {
            showError(e.getMessage());
            return;
        }
        showSuccess("Staff added successfully.");
    }

    public void showUpdateHospitalStaffForm() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the staff ID: ");
        String userId = sc.nextLine();
        
        User user;
        try {
            user = AdministratorController.getStaff(userId);
        } catch (Exception e) {
            showError(e.getMessage());
            return;
        }

        System.out.println("Please enter the following details (leave blank to skip):");
        System.out.print("Name: ");
        String name = sc.nextLine();
        if (name.isEmpty()) {
            name = user.getName();
        }
        System.out.println("Gender:");
        System.out.println("1. Male");
        System.out.println("2. Female");
        System.out.println("3. Skip");
        int genderChoice = getChoice(1, 3);
        boolean isMale;
        switch (genderChoice) {
            case 1:
                isMale = true;
                break;
            case 2:
                isMale = false;
                break;
            default:
                isMale = user.getIsMale();
                break;
        }
        System.out.print("Age: ");
        String ageString = sc.nextLine();
        int age = user.getAge();
        if (!ageString.isEmpty()) {
            try {
                age = Integer.parseInt(ageString);
            } catch (Exception e) {
                showError(e.getMessage());
                System.out.println("Age will not be updated.");
            }
        }
        System.out.print("Email: ");
        String email = sc.nextLine();
        if (email.isEmpty()) {
            email = user.getEmail();
        }

        if (user.getRole() == UserRole.PHARMACIST) {
            User updatedUser = new User(userId, null, user.getRole(), name, isMale, age, email);
            try {
                AdministratorController.updateStaff(userId, updatedUser);
            } catch (Exception e) {
                showError(e.getMessage());
            }
            showSuccess("Staff updated successfully.");
            return;
        }

        System.out.print("Specialty: ");
        String specialty = sc.nextLine();
        if (specialty.isEmpty()) {
            specialty = ((Doctor) user).getSpecialty();
        }
        User updatedUser = new Doctor(userId, null, user.getRole(), name, isMale, age, email, specialty, ((Doctor) user).getRatingCount(), ((Doctor) user).getRating());
        try {
            AdministratorController.updateStaff(userId, updatedUser);
        } catch (Exception e) {
            showError(e.getMessage());
            return;
        }
        showSuccess("Staff updated successfully.");
    }

    public void showRemoveHospitalStaffForm() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the staff ID: ");
        String userId = sc.nextLine();

        try {
            AdministratorController.removeStaff(userId);
        } catch (Exception e) {
            showError(e.getMessage());
            return;
        }
        showSuccess("Staff removed successfully.");
    }

    public void showAppointmentDetails() {
        List<Appointment> appointments = AdministratorController.getAppointments();

        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            List<String> tips = new ArrayList<String>();
            tips.add("Try adding a doctor first.");
            tips.add("Try encouraging doctors to put up more availability slots.");
            showUserTips(tips);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        showSuccess();
        System.out.println("The following appointments were found:");
        for (Appointment appointment : appointments) {
            System.out.println("Patient ID: " + appointment.getPatient().getUserId());
            System.out.println("Doctor ID: " + appointment.getDoctor().getUserId());
            System.out.println("Status: " + appointment.getStatus());
            System.out.println("Date of Appointment: " + sdf.format(appointment.getDate()));
            if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
                AppointmentOutcomeRecord outcomeRecord;
                try {
                    outcomeRecord = AdministratorController.getAppointmentOutcomeRecord(appointment.getOutcomeRecordId());
                } catch (Exception e) {
                    showError(e.getMessage());
                    continue;
                }
                System.out.println("Service Type: " + outcomeRecord.getServiceType());
                System.out.println("Prescribed Medications: ");
                List<Prescription> prescriptions = outcomeRecord.getPrescriptions();
                if (prescriptions.isEmpty()) {
                    System.out.println("    (Empty)");
                }
                for (Prescription prescription : prescriptions) {
                    System.out.println("   - " + prescription.getMedicine().getName() + " (" + prescription.getStatus() + ")");
                }
                System.out.println("Consultation Notes: " + outcomeRecord.getNotes());
            }
            System.out.println();
        }
    }

    public void showMedicationInventory() {
        List<Medicine> medicines = AdministratorController.getMedicineInventory();

        if (medicines.isEmpty()) {
            System.out.println("No medications found.");
            List<String> tips = new ArrayList<String>();
            tips.add("Try adding a new medication first.");
            showUserTips(tips);
            return;
        }

        showSuccess();
        System.out.println("The following medications were found:");
        for (Medicine medicine : medicines) {
            System.out.println("Medication ID: " + medicine.getMedicineId());
            System.out.println("Name: " + medicine.getName());
            System.out.println("Stock level: " + medicine.getStock());
            System.out.println("Low stock level alert line: " + medicine.getLowStockThreshold());
            System.out.println("Requesting replenishment: " + medicine.getIsRequestingReplenishment());
            System.out.println("Price: $" + medicine.getPrice());
            System.out.println();
        }
    }

    public void showManageMedicationInventoryForm() {
        System.out.println("What would you like to do?");
        System.out.println("1. Add a new medication");
        System.out.println("2. Remove a medication");
        System.out.println("3. Update stock level of a medication");
        System.out.println("4. Update low stock level alert line of a medication");
        System.out.println("5. Quit");

        int choice = getChoice(1, 5);

        Scanner sc = new Scanner(System.in);
        String medicineId;
        switch (choice) {
            case 1:
                System.out.println("Please enter the following details:");
                System.out.print("Name: ");
                String name = sc.nextLine();
                System.out.print("Stock level: ");
                int stock = sc.nextInt();
                System.out.print("Low stock level alert line: ");
                int lowStockThreshold = sc.nextInt();
                System.out.print("Price: $");
                float price = sc.nextFloat();
                Medicine medicine = new Medicine(null, name, stock, lowStockThreshold, false, price);
                try {
                    AdministratorController.addMedicine(medicine);
                } catch (Exception e) {
                    showError(e.getMessage());
                    return;
                }
                showSuccess("Medication added successfully.");
                break;
            case 2:
                System.out.print("Please enter the medication ID: ");
                medicineId = sc.nextLine();
                try {
                    AdministratorController.removeMedicine(medicineId);
                } catch (Exception e) {
                    showError(e.getMessage());
                    return;
                }
                showSuccess("Medication removed successfully.");
                break;
            case 3:
                System.out.print("Please enter the medication ID: ");
                medicineId = sc.nextLine();
                System.out.print("Please enter the new stock level: ");
                int stockLevel = sc.nextInt();
                try {
                    AdministratorController.updateMedicineStockLevel(medicineId, stockLevel);
                } catch (Exception e) {
                    showError(e.getMessage());
                    return;
                }
                showSuccess("Stock level updated successfully.");
                break;
            case 4:
                System.out.print("Please enter the medication ID: ");
                medicineId = sc.nextLine();
                System.out.print("Please enter the new low stock level alert line: ");
                int threshold = sc.nextInt();
                try {
                    AdministratorController.updateMedicineLowStockThreshold(medicineId, threshold);
                } catch (Exception e) {
                    showError(e.getMessage());
                    return;
                }
                showSuccess("Low stock level alert line updated successfully.");
                break;
            default:
                return;
        }
    }

    public void showApproveReplenishmentRequestForm() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the medication ID: ");
        String medicineId = sc.nextLine();

        try {
            AdministratorController.approveReplenishmentRequest(medicineId);
        } catch (Exception e) {
            showError(e.getMessage());
            return;
        }
        showSuccess("Replenishment request approved successfully.");
    }
}