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

/**
 * The PharmacistView class provides the user interface for the pharmacists.
 * <p>
 * It allows the pharmacist to view notifications, appointment outcome records, update prescription status, 
 * view medication inventory, submit replenishment requests, and change password.
 * </p>
 */
public class PharmacistView extends DashboardView{
    /**
     * The user associated with this PharmacistView.
     */
    private User user;

    /**
     * The list of notifications received by the pharmacist.
     */
    private List<Notification> notifications;

    /**
     * Constructor for the PharmacistView class.
     * Initializes the notifications list and subscribes to the AppointmentOutcomeRecordStore.
     *
     * @param user The user associated with this PharmacistView.
     */
    public PharmacistView(User user){
        this.user = user;
        this.notifications = new ArrayList<Notification>();
        AppointmentOutcomeRecordStore.subscribe(this);
    }

    /**
     * Implements the ISubscriber interface.
     * Updates the list of notifications when receiving a new notification from subscribed publishers.
     *
     * @param notification The new notification to be added to the list.
     */
    public void update(Notification notification){
        notifications.add(notification);
    }

    /**
     * Displays a menu with a set of actions that the pharmacist can choose from, including:
     * <ol>
     *  <li>Show notifications</li>
     *  <li>View appointment outcome record</li>
     *  <li>Update prescription status</li>
     *  <li>View medication inventory</li>
     *  <li>Submit replenishment request</li>
     *  <li>Change password</li>
     *  <li>Logout</li>
     * </ol>
     * The method runs in a while loop, i.e., it repeatedly displays the menu after the pharmacist completes an action until logout.
     */
    public void launch(){
        while(true) {
            System.out.println("======================================================================================================");
            System.out.println("|                                     HOSPITAL MANAGEMENT SYSTEM                                     |");
            System.out.println("|                                           PHARMACIST DASHBOARD                                     |");
            System.out.println("======================================================================================================");
            System.out.println("Welcome, " + user.getName());
            if (notifications.size() > 0) {
                System.out.println("You have " + notifications.size() + " new notifications.");
            }
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

    /**
     * Displays the details of an appointment outcome record based on user input.
     * <p>
     * Prompts the pharmacist to enter the appointment outcome record ID, retrieves the outcome record
     * using, and displays the record details including date, service type, notes, and prescriptions.
     * </p>
     * <p>
     * If the record does not exist or an exception occurs during retrieval, an error message is displayed.
     * </p>
     */
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

    /**
     * Displays a form to update the status of a prescription:
     * <ol>
     *  <li>Prompts the pharmacist to enter the appointment outcome record ID that need to dispense prescribed medications.</li>
     *  <li>If there are no prescriptions available under the outcome record, it notifies the user and exits.</li>
     *  <li>Otherwise, it lists the prescriptions and prompts the user to select one to dispense.</li>
     * </ol>
     */
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
        System.out.println(count + ". Quit");
        int choice = getChoice(1, record.getPrescriptions().size() + 1);
        if(choice == record.getPrescriptions().size() + 1){
            return;
        }

        try{
            PharmacistController.approvePrescriptionRequest(record.getAppointmentOutcomeRecordId(), choice-1);
            showSuccess("Prescription status updated successfuly.");
        } catch(Exception e){
            showError(e.getMessage());
        }
    }

    /**
     * Displays the medication inventory.
     */
    public void showMedicationInventory(){
        int count;
        List<Medicine> medicineInventory = PharmacistController.getMedicineInventory();
        showSuccess();
        System.out.println("Medicine Inventory: ");
        System.out.println("--------------------");
        count = 1;
        for(Medicine medicine : medicineInventory){
            System.out.println(count + ". " + medicine.getName() + "\t\tLow stock threshold: " + medicine.getLowStockThreshold() + "\t\tStock: " + medicine.getStock());
            count++;
        }
        System.out.println();
    }

    /**
     * Displays a form for submitting medication replenishment requests to the Administrator.
     * <ol>
     *  <li>Retrieves the current medicine inventory.</li>
     *  <li>If the inventory is empty, it notifies the user and exits.</li>
     *  <li>Otherwise, it lists the available medicines along with their low stock level alert lines and current stock levels.</li>
     *  <li>Prompts the Pharmacist to select a medicine for replenishment.</li>
     * </ol>
     */
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
            System.out.println(count + ". " + medicine.getName() + "\t\tLow stock threshold: " + medicine.getLowStockThreshold() + "\t\tStock: " + medicine.getStock());
            count++;
        }
        System.out.println(count + ". Quit");
        choice = getChoice(1, medicineInventory.size() + 1);
        if(choice == medicineInventory.size() + 1){
            return;
        }
        Medicine selectedMedicine = medicineInventory.get(choice-1);

        try{
            PharmacistController.submitReplenishmentRequest(selectedMedicine.getMedicineId());
            showSuccess("Replenishment request sent successfully.");
        }catch (Exception e){
            showError(e.getMessage());
        }
    }
}
