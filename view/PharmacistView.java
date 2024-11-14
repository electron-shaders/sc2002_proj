package view;

import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.AppointmentOutcomeRecord;
import model.Medicine;
import model.Prescription;
import model.User;
import observer.Notification;
import store.AppointmentOutcomeRecordStore;
import controller.PharmacistController;

public class PharmacistView extends DashboardView{
    private User user;
    private List<Notification> notifications;

    public PharmacistView(User user){
        this.user = user;
        this.notifications = new ArrayList<Notification>();
        AppointmentOutcomeRecordStore.subscribe(this);
    }

    public void update(Notification notification){
        notifications.add(notification);
    }

    public void launch(){
        while(true) {
            System.out.println("Welcome, " + user.getName());
            System.out.println("What would you like to do?");
            System.out.println("1. Show notifications");
            System.out.println("2. View appointment outcome record");
            System.out.println("3. Update prescription status");
            System.out.println("4. View medication inventory");
            System.out.println("5. Submit replenishment request");
            System.out.println("6. Change password");
            System.out.println("7. Logout");
            System.out.println();

            int choice = getChoice(1, 7);
            switch (choice) {
                case 1:
                    List<String> notificationStrings = new ArrayList<String>();
                    for(Notification notification : notifications){
                        notificationStrings.add(notification.toString());
                    }
                    showNotifications(notificationStrings);
                    notifications.clear();
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
                    showChangePasswordForm(this.user);
                case 7:
                    user.logout();
                    return;
            }
        }
    }

    public void showAppointmentOutcomeRecord(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the appointment outcome record ID: ");
        String appointmentOutcomeRecordId = sc.nextLine();
        AppointmentOutcomeRecord record;
        try {
            record = PharmacistController.getAppointmentOutcomeRecord(appointmentOutcomeRecordId);
        } catch (Exception e) {
            showError(e.getMessage());
            return;
        }

        if(record == null){
            showError("Appointment outcome record with this ID does not exist.");
            return;
        }

        showSuccess();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Appointment outcome record ID: " + record.getAppointmentOutcomeRecordId());
        System.out.println("Date: " + sdf.format(record.getDate()));
        System.out.println("Service type: " + record.getServiceType());
        System.out.println("Notes: " + record.getNotes());
        System.out.println("--------------------");
        System.out.println("Prescriptions: ");
        if(record.getPrescriptions().isEmpty()){
            System.out.println("  (Empty)");
        }
        for (Prescription prescription : record.getPrescriptions()) {
            System.out.println("- " + prescription.getMedicine().getName() + "  (" + prescription.getStatus() + ")");
        }
        System.out.println();
    }

    public void showUpdatePrescriptionStatusForm(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the appointment outcome record ID: ");
        String appointmentOutcomeRecordId = sc.nextLine();
        AppointmentOutcomeRecord record;
        try {
            record = PharmacistController.getAppointmentOutcomeRecord(appointmentOutcomeRecordId);
        } catch (Exception e) {
            showError(e.getMessage());
            return;
        }

        if(record.getPrescriptions().isEmpty()){
            System.out.println("There are no prescriptions available to update.");
            return;
        }

        System.out.println("Which prescription would you like to update?");
        int count = 1;
        for(Prescription prescription : record.getPrescriptions()){
            System.out.println(count + ". " + prescription.getMedicine().getName() + "  (" + prescription.getStatus() + ")");
            count++;
        }
        int choice = getChoice(1, record.getPrescriptions().size());

        try{
            PharmacistController.approvePrescriptionRequest(record.getAppointmentOutcomeRecordId(), choice-1);
            showSuccess("Prescription status updated successfuly.");
        } catch(Exception e){
            showError(e.getMessage());
        }
    }

    public void showMedicationInventory(){
        int count;
        List<Medicine> medicineInventory = PharmacistController.getMedicineInventory();
        showSuccess();
        System.out.println("Medicine Inventory: ");
        System.out.println("--------------------");
        count = 1;
        for(Medicine medicine : medicineInventory){
            System.out.println(count + ". " + medicine.getName() + "     Low stock threshold: " + medicine.getLowStockThreshold() + "     Stock: " + medicine.getStock());
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
        choice = getChoice(1, medicineInventory.size());
        Medicine selectedMedicine = medicineInventory.get(choice-1);

        try{
            PharmacistController.submitReplenishmentRequest(selectedMedicine.getMedicineId());
            showSuccess("Replenishment request sent successfully.");
        }catch (Exception e){
            showError(e.getMessage());
        }
    }
}
