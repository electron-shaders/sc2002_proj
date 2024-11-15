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

public class AdministratorController {
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
    public static List<Appointment> getAppointments(){
        return AppointmentStore.getRecords();
    }
    public static AppointmentOutcomeRecord getAppointmentOutcomeRecord(String outcomeRecordId) throws Exception{
        AppointmentOutcomeRecord record = AppointmentOutcomeRecordStore.getRecord(outcomeRecordId);
        if(record != null){
            return record;
        }
        throw new Exception("Appointment Outcome Record Not Found.");
    }
    public static List<Medicine> getMedicineInventory(){
        return MedicineStore.getRecords();
    }
    public static String addMedicine(Medicine medicine){
        return MedicineStore.addRecord(medicine);
    }
    public static void removeMedicine(String medicineId) throws Exception{
        if(MedicineStore.getRecord(medicineId) != null ){
            MedicineStore.removeRecord(medicineId);
            return;
        }
        throw new Exception("Medicine Not Found.");
    }
    public static void updateMedicineStockLevel(String medicineId, int stockLevel) throws Exception{
        Medicine medicine = MedicineStore.getRecord(medicineId);
        if(medicine != null){
            medicine.setStock(stockLevel);
            return;
        }
        throw new Exception("Medicine Not Found.");
    }
    public static void updateMedicineLowStockThreshold(String medicineId, int threshold) throws Exception{
        Medicine medicine = MedicineStore.getRecord(medicineId);
        if(medicine != null){
            medicine.setLowStockThreshold(threshold);
            return;
        }
        throw new Exception("Medicine Not Found.");
    }
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
