package controller;

import java.util.List;

import model.AppointmentOutcomeRecord;
import model.Medicine;
import model.Prescription;
import model.PrescriptionStatus;
import store.AppointmentOutcomeRecordStore;
import store.MedicineStore;

public class PharmacistController {
    public static AppointmentOutcomeRecord getAppointmentOutcomeRecord(String appointmentOutcomeRecordedId) throws Exception{
        AppointmentOutcomeRecord OutcomeRecord =  AppointmentOutcomeRecordStore.getRecord(appointmentOutcomeRecordedId);
        if(OutcomeRecord != null){
            return OutcomeRecord;
        }
        throw new Exception("Outcome Record is not found.");
    }
    public static void approvePrescriptionRequest(String appointmentOutcomeRecordId, int prescriptionIdx) throws Exception{
        AppointmentOutcomeRecord Record = AppointmentOutcomeRecordStore.getRecord(appointmentOutcomeRecordId);
        if(Record == null){
            throw new Exception("Outcome Record is not found.");
        }
        List<Prescription> prescriptionList = Record.getPrescriptions();
        if(prescriptionIdx < 0 || prescriptionIdx >= prescriptionList.size()){
            throw new Exception("Invalid prescription Id.");
        }
        Prescription prescription = prescriptionList.get(prescriptionIdx);
        prescription.setStatus(PrescriptionStatus.DISPENSED);
        Medicine medicine = prescription.getMedicine();
        medicine.setStock(medicine.getStock() - 1);
    }
    public static List<Medicine> getMedicineInventory(){
        return MedicineStore.getRecords();
    }
    public static void submitReplenishmentRequest(String medicinedId) throws Exception{
        Medicine medicine = MedicineStore.getRecord(medicinedId);
        if(medicine != null && medicine.getStock() <= medicine.getLowStockThreshold()){
            medicine.setIsRequestingReplenishment(true);
            return;
        }
        throw new Exception("Cannot sumbmit medicine replenishment request.");
    }
}
