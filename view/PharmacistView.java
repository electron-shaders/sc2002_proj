package view;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import model.PrescriptionStatus;
import controller.PharmacistController;

public class PharmacistView extends DashboardView{
    User user;

    public PharmacistView(User user){
        this.user = user;
    }

    public void launch(){
        System.out.println("Welcome, " + user.getName());
        System.out.println("What would you like to do?");
        System.out.println("1. Show notifications");
        System.out.println("2. View appointment outcome record");
        System.out.println("3. Update prescription status");
        System.out.println("4. View medication inventory");
        System.out.println("5. Submit replenishment request");
        System.out.println("6. Show tutorials");
        System.out.println("7. Change password");
        System.out.println("8. Logout");
        System.out.println();

        while(true) {
            int choice = getChoice(1, 8);
            switch (choice) {
                case 1:
                    showNotifications();
                    break;
                case 2:
                    showAppointmentOutcomeRecord();
                    break;
                case 3:
                    showUpdatePrescriptionStatusForm();
                    break;
                case 4:
                    showMedicationInventory();
                    break;
                case 5:
                    showSubmitReplenishmentRequestForm();
                    break;
                case 6:
                    showTutorial();
                    break;
                case 7:
                    showChangePasswordForm(this.user);
                case 8:
                    user.logout();
                    return;
            }
        }
    }

    public void showAppointmentOutcomeRecord(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the appointment outcome record ID: ");
        String appointmentOutcomeRecordId = sc.nextLine();
        AppointmentOutcomeRecord record = PharmacistController.getAppointmentOutcomeRecord(appointmentOutcomeRecordId);

        if(record == null){
            showError("Appointment outcome record with this ID does not exist.");
            return;
        }

        System.out.println("Appointment outcome record ID: " + record.getAppointmentOutcomeRecordId());
        System.out.println("Date: " + record.getDate());
        System.out.println("Service type: " + record.getServiceType());
        System.out.println("Notes: " + record.getNotes());
        System.out.println("--------------------");
        System.out.println("Prescriptions: ");
        if(record.getPrescriptions().isEmpty()){
            System.out.println("  (Empty)");
        }
        for (String prescription : record.getPrescriptions()) {
            System.out.println("- " + prescription);
        }
        System.out.println();
    }

    public void showUpdatePrescriptionStatusForm(){
        Scanner sc = new Scanner(System.in);
        int count, choice;
        System.out.println("Please enter the appointment outcome record ID: ");
        String appointmentOutcomeRecordId = sc.nextLine();
        AppointmentOutcomeRecord record = PharmacistController.getAppointmentOutcomeRecord(appointmentOutcomeRecordId);

        if(record.getPrescriptions.isEmpty()){
            System.out.println("There are no prescriptions available to update.");
            return;
        }

        System.out.println("Which prescription would you like to update?");
        count = 1;
        for(Prescription prescription : record.getPrescriptions()){
            System.out.println(count + ". " + pescription.getMedicine().getName() + "  (" + prescription.getStatus() + ")");
            count++;
        }
        choice = getChoice(1, record.getPrescriptions.length);
        Prescription selectedPrescription = record.getPrescriptions.get(choice-1);

        System.out.println("Please select status to be set: ");
        count = 1;
        for(PrescriptionStatus status : PrescriptionStatus.values()){
            System.out.println(count + ". " + status);
            count++;
        }
        choice = getChoice(1, Prescriptionstatus.values().length);
        PrescriptionStatus selectedStatus = PrescriptionStatus.values()[choice-1];

        try{
            selectedPrescription.setStatus(selectedStatus);
            showSuccess("Prescription status updated successfuly.");
        } catch(Exception e){
            showError(e.getMessage());
        }
    }

    public void showMedicationInventory(){
        int count;
        List<Medicine> medicineInventory = PharmacistController.getMedicineInventory();
        System.out.println("Medicine Inventory: ");
        System.out.println("--------------------");
        count = 1;
        for(Medicine medicine : medicineInventory){
            System.out.println(count + ". " + medicine.getName() + "     Stock: " + medicine.getStock());
            count++;
        }
        System.out.println();
    }

    public void showSubmitReplenishmentRequestForm(){
        int count, choice;
        List<Medicine> medicineInventory = PharmacistController.getMedicineInventory();

        if(medicineInventory.isEmpty()){
            System.out.println("There are no medicines available.");
            return;
        }

        System.out.println("Please select the medicine for replenishment: ");
        count = 1;
        for(Medicine medicine : medicineInventory){
            System.out.println(count + ". " + medicine.getName() + "     Stock: " + medicine.getStock());
            count++;
        }
        choice = getChoice(1, medicineInventory.length);
        Medicine selectedMedicine = medicineInventory.get(choice-1);

        try{
            selectedMedicine.setIsRequestingReplenishment(true);
            showSuccess("Request sent successfully.");
        }catch (Exception e){
            showError(e.getMessage());
        }
    }
}
