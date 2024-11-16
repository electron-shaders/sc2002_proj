package controller;

import java.util.ArrayList;
import java.util.List;

import model.Appointment;
import model.AppointmentOutcomeRecord;
import model.Doctor;
import model.Medicine;
import model.User;
import model.UserRole;
import store.AppointmentOutcomeRecordStore;
import store.AppointmentStore;
import store.DoctorStore;
import store.MedicineStore;
import store.StaffStore;

/**
 * The AdministratorController class provides Administrator with methods for managing staff, appointments, and medicine inventory.
 * <p>
 * It depends on Appointment, AppointmentOutcomeRecord, Doctor, Medicine, User, AppointmentOutcomeRecordStore, AppointmentStore,
 * DoctorStore, MedicineStore, and StaffStore.
 * </p>
 */
public class AdministratorController {
    /**
     * Adds a staff member to the system.
     *
     * @param user the staff member to add
     * @return the ID of the staff member
     * @throws Exception if the role is invalid (neither DOCTOR nor PHARMACIST)
     */
    public static String addStaff(User user) throws Exception{
        UserRole role = user.getRole();
        if(role == UserRole.PATIENT){
            throw new Exception("Invalid Staff.");
        }
        else if(role == UserRole.DOCTOR){
            return DoctorStore.addRecord((Doctor)user);
        }
        else if(role == UserRole.PHARMACIST){
            return StaffStore.addRecord(user);
        }
        else {
            throw new Exception("Invalid Staff.");
        }
    }

    /**
     * Gets a staff member by the user ID.
     * 
     * @param userId the user ID of the staff member
     * @return the staff member
     * @throws Exception if the staff member is not found or the staff member is neither a doctor nor a pharmacist
     */
    public static User getStaff(String userId) throws Exception{
        User user = DoctorStore.getRecord(userId);
        if(user != null){
            return user;
        }
        user = StaffStore.getRecord(userId);
        if(user != null && user.getRole() == UserRole.PHARMACIST){
            return user;
        }
        throw new Exception("Staff Not Found.");
    }

    /**
     * Searches for staff members by role.
     * 
     * @param role the role of the staff members to search for
     * @return the list of staff members with the specified role
     * @throws Exception if the role is invalid (neither DOCTOR nor PHARMACIST)
     */
    public static List<User> searchStaff(UserRole role) throws Exception{
        if(role == UserRole.DOCTOR){
            List<User> users = new ArrayList<>(DoctorStore.getRecords());
            return users;
        }
        else if(role == UserRole.PHARMACIST){
            List<User> StaffList = StaffStore.getRecords();
            List<User> Result = new ArrayList<User>(); 
            for(User user : StaffList) {
                if(user.getRole() == UserRole.PHARMACIST){
                    Result.add(user);
                }
            }
            return Result;
        }
        else{
            throw new Exception("Invalid Role.");
        }
    }

    /**
     * Searches for staff members by age.
     * 
     * @param age the age of the staff members to search for
     * @return the list of staff members with the specified age
     */
    public static List<User> searchStaff(int age){
        List<User> Result = new ArrayList<User>(); 
        List<User> DoctorList = new ArrayList<>(DoctorStore.getRecords());
        for(User user : DoctorList) {
            if(user.getAge() == age){
                Result.add(user);
            }
        }
        List<User> StaffList = StaffStore.getRecords();
        for(User user : StaffList) {
            if(user.getAge() == age && user.getRole() == UserRole.PHARMACIST){
                Result.add(user);
            }
        }
        return Result;
    }

    /**
     * Searches for staff members by gender.
     * 
     * @param isMale the gender of the staff members to search for, true for Male, false for Female
     * @return the list of staff members with the specified gender
     */
    public static List<User> searchStaff(boolean isMale){
        List<User> Result = new ArrayList<User>(); 
        List<User> DoctorList = new ArrayList<>(DoctorStore.getRecords());
        for(User user : DoctorList) {
            if(user.getIsMale() == isMale){
                Result.add(user);
            }
        }
        List<User> StaffList = StaffStore.getRecords();
        for(User user : StaffList) {
            if(user.getIsMale() == isMale && user.getRole() == UserRole.PHARMACIST){
                Result.add(user);
            }
        }
        return Result;
    }

    /**
     * Searches for staff members by name. The search is case-insensitive.
     * 
     * @param name the name of the staff members to search for
     * @return the list of staff members with the specified name
     */
    public static List<User> searchStaff(String name){
        List<User> Result = new ArrayList<User>(); 
        List<User> DoctorList = new ArrayList<>(DoctorStore.getRecords());
        for(User user : DoctorList) {
            if(user.getName().toLowerCase().contains(name.toLowerCase())){
                Result.add(user);
            }
        }
        List<User> StaffList = StaffStore.getRecords();
        for(User user : StaffList) {
            if(user.getName().toLowerCase().contains(name.toLowerCase()) && user.getRole() == UserRole.PHARMACIST){
                Result.add(user);
            }
        }
        return Result;
    }

    /**
     * Updates a staff member by the user ID.
     * 
     * @param userId the user ID of the staff member
     * @param user the new staff member record
     * @throws Exception if the staff member is not found or the staff member is neither a doctor nor a pharmacist
     */
    public static void updateStaff(String userId, User user) throws Exception{
        if(user.getRole() == UserRole.DOCTOR){
            DoctorStore.updateRecord(userId, (Doctor)user);
        }
        else if(user.getRole() == UserRole.PHARMACIST){
            StaffStore.updateRecord(userId, user);
        }
        else{
            throw new Exception("Staff Not Found.");
        }
    }

    /**
     * Removes a staff member by the user ID.
     * 
     * @param userId the user ID of the staff member
     * @throws Exception if the staff member is not found or the staff member is neither a doctor nor a pharmacist
     */
    public static void removeStaff(String userId) throws Exception{
        User user = DoctorStore.getRecord(userId);
        if(user != null){
            DoctorStore.removeRecord(userId);
            return;
        }
        user = StaffStore.getRecord(userId);
        if(user != null && user.getRole() == UserRole.PHARMACIST){
            StaffStore.removeRecord(userId);
            return;
        }
        throw new Exception("Staff Not Found.");
    }

    /**
     * Gets all appointment details in the system.
     * 
     * @return the list of all appointments
     */
    public static List<Appointment> getAppointments(){
        return AppointmentStore.getRecords();
    }

    /**
     * Gets an appointment outcome record by the outcome record ID.
     * 
     * @param outcomeRecordId the outcome record ID of the appointment outcome record
     * @return the appointment outcome record
     * @throws Exception if the appointment outcome record is not found
     */
    public static AppointmentOutcomeRecord getAppointmentOutcomeRecord(String outcomeRecordId) throws Exception{
        AppointmentOutcomeRecord record = AppointmentOutcomeRecordStore.getRecord(outcomeRecordId);
        if(record != null){
            return record;
        }
        throw new Exception("Appointment Outcome Record Not Found.");
    }

    /**
     * Gets the details of all medications in the inventory.
     * 
     * @return the list of all medications in the inventory
     */
    public static List<Medicine> getMedicineInventory(){
        return MedicineStore.getRecords();
    }

    /**
     * Adds a medication to the inventory.
     * 
     * @param medicine the medication to add
     * @return the ID assigned to the medication
     */
    public static String addMedicine(Medicine medicine){
        return MedicineStore.addRecord(medicine);
    }

    /**
     * Removes a medication from the inventory by the medicine ID.
     * 
     * @param medicineId the medicine ID of the medication to remove
     * @throws Exception if the medication is not found
     */
    public static void removeMedicine(String medicineId) throws Exception{
        if(MedicineStore.getRecord(medicineId) != null ){
            MedicineStore.removeRecord(medicineId);
            return;
        }
        throw new Exception("Medicine Not Found.");
    }

    /**
     * Updates the stock level of a medication by the medicine ID.
     * 
     * @param medicineId the medicine ID of the medication
     * @param stockLevel the new stock level of the medication
     * @throws Exception if the medication is not found
     */
    public static void updateMedicineStockLevel(String medicineId, int stockLevel) throws Exception{
        Medicine medicine = MedicineStore.getRecord(medicineId);
        if(medicine != null){
            medicine.setStock(stockLevel);
            return;
        }
        throw new Exception("Medicine Not Found.");
    }

    /**
     * Updates the low stock level alert line of a medication by the medicine ID.
     * 
     * @param medicineId the medicine ID of the medication
     * @param threshold the new low stock level alert line of the medication
     * @throws Exception if the medication is not found
     */
    public static void updateMedicineLowStockThreshold(String medicineId, int threshold) throws Exception{
        Medicine medicine = MedicineStore.getRecord(medicineId);
        if(medicine != null){
            medicine.setLowStockThreshold(threshold);
            return;
        }
        throw new Exception("Medicine Not Found.");
    }

    /**
     * Approves a replenishment request for a medication by the medicine ID.
     * 
     * @param medicineId the medicine ID of the medication requesting replenishment
     * @throws Exception if the medication is not found or the medication is not requesting replenishment
     */
    public static void approveReplenishmentRequest(String medicineId) throws Exception{
        Medicine medicine = MedicineStore.getRecord(medicineId);
        if(medicine != null && medicine.getIsRequestingReplenishment()){
            medicine.setStock(medicine.getStock() + medicine.getLowStockThreshold());
            medicine.setIsRequestingReplenishment(false);
            return;
        }
        throw new Exception("Medicine Replenishment Request Not Found.");
    }
}
