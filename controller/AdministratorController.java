package controller;

import java.util.ArrayList;
import java.util.List;

import store.AppointmentStore;
import store.DoctorStore;
import store.MedicineStore;
import store.StaffStore;

public class AdministratorController {
    public static String addStaff(User user){
        UserRole role = user.getRole();
        if(role == UserRole.PATIENT){
            throw new Exception("Invalid Staff.");
        }
        else if(role == UserRole.DOCTIOR){
            return DoctorStore.addRecord((Doctor)user);
        }
        else if(role == UserRole.PHARMACIST){
            return StaffStore.addRecord(user);
        }
        else {
            throw new Exception("Invalid Staff.");
        }
    }
    public static List<User> searchStaff(UserRole role){
        if(role == UserRole.DOCTOR){
            return DoctorStore.getRecords();
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
        List<User> DoctorList = DoctorStore.getRecords();
        for(User user : DoctorList) {
            if(user.getAge() == age){
                Result.add(user);
            }
        }
        List<User> StaffList = StaffStore.getRecords();
        for(User user : StaffList) {
            if(user.getAge() == age){
                Result.add(user);
            }
        }
        return Result;
    }
    public static List<User> searchStaff(boolean isMale){
        List<User> Result = new ArrayList<User>(); 
        List<User> DoctorList = DoctorStore.getRecords();
        for(User user : DoctorList) {
            if(user.getIsMale() == isMale){
                Result.add(user);
            }
        }
        List<User> StaffList = StaffStore.getRecords();
        for(User user : StaffList) {
            if(user.getIsMale() == isMale){
                Result.add(user);
            }
        }
        return Result;
    }
    public static List<User> searchStaff(String name){
        List<User> Result = new ArrayList<User>(); 
        List<User> DoctorList = DoctorStore.getRecords();
        for(User user : DoctorList) {
            if(name.equalsIgnoreCase(user.getName())){
                Result.add(user);
            }
        }
        List<User> StaffList = StaffStore.getRecords();
        for(User user : StaffList) {
            if(name.equalsIgnoreCase(user.getName())){
                Result.add(user);
            }
        }
        return Result;
    }
    public static void updateStaff(String userId, User user){
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
    public static void removeStaff(String userId){
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
    public static List<Medicine> getMedicineInventory(){
        return MedicineStore.getRecords();
    }
    public static String addMedicine(Medicine medicine){
        return MedicineStore.addRecord(medicine);
    }
    public static void removeMedicine(String medicineId){
        if(MedicineStore.getRecord(medicineId) != null ){
            MedicineStore.removeRecord(medicineId);
            return;
        }
        throw new Exception("Medicine Not Found.");
    }
    public static void updateMedicineStockLevel(String medicineId, int stockLevel){
        Medicine medicine = MedicineStore.getRecord(medicineId);
        if(medicine != null){
            medicine.setStock(stockLevel);
            return;
        }
        throw new Exception("Medicine Not Found.");
    }
    public static void updateMedicineLowStockThreshold(String medicineId, int threshold){
        Medicine medicine = MedicineStore.getRecord(medicineId);
        if(medicine != null){
            medicine.setLowStockThreshold(threshold);
            return;
        }
        throw new Exception("Medicine Not Found.");
    }
    public static void approveReplenishmentRequest(String medicineId){
        Medicine medicine = MedicineStore.getRecord(medicineId);
        if(medicine != null){
            medicine.setStock(medicine.getStock() + medicine.getLowStockThreshold());
            medicine.setIsRequestingReplenishment(false);
            return;
        }
        throw new Exception("Medicine Not Found.");
    }
}
