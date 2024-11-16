package controller;

import java.util.List;

import model.AppointmentOutcomeRecord;
import model.Medicine;
import model.Prescription;
import model.PrescriptionStatus;
import store.AppointmentOutcomeRecordStore;
import store.MedicineStore;

/**
 * The PharmacistController class provides Pharmacists with methods for managing appointment outcome records,
 * approving prescription requests, getting medicine inventory, and submitting replenishment requests.
 * <p>
 * It depends on AppointmentOutcomeRecord, Medicine, Prescription, AppointmentOutcomeRecordStore, and MedicineStore.
 * </p>
 */
public class PharmacistController {
    /**
     * Gets the appointment outcome record with the specified ID.
     *
     * @param appointmentOutcomeRecordedId the ID of the appointment outcome record
     * @return the appointment outcome record with the specified ID
     * @throws Exception if the appointment outcome record is not found
     */
    public static AppointmentOutcomeRecord getAppointmentOutcomeRecord(String appointmentOutcomeRecordedId) throws Exception{
        AppointmentOutcomeRecord OutcomeRecord =  AppointmentOutcomeRecordStore.getRecord(appointmentOutcomeRecordedId);
        if(OutcomeRecord != null){
            return OutcomeRecord;
        }
        throw new Exception("Outcome Record is not found.");
    }

    /**
     * Dispense a prescription of an appointment outcome record.
     * 
     * @param appointmentOutcomeRecordId the ID of the appointment outcome record
     * @param prescriptionIdx            the index of the prescription to be dispensed in the prescription list of the appointment outcome record
     * @throws Exception if the appointment outcome record is not found or the prescription index is out of bounds
     */
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

    /**
     * Gets the list of medicines in the inventory.
     *
     * @return the list of medicines in the inventory
     */
    public static List<Medicine> getMedicineInventory(){
        return MedicineStore.getRecords();
    }

    /**
     * Submits a medicine replenishment request for the medicine with the specified ID.
     *
     * @param medicinedId the ID of the medicine
     * @throws Exception if the medicine is not found or the medicine is not below the low stock level alert line
     */
    public static void submitReplenishmentRequest(String medicinedId) throws Exception{
        Medicine medicine = MedicineStore.getRecord(medicinedId);
        if(medicine != null && medicine.getStock() <= medicine.getLowStockThreshold() && !medicine.getIsRequestingReplenishment()){
            medicine.setIsRequestingReplenishment(true);
            return;
        }
        throw new Exception("Cannot sumbmit medicine replenishment request.");
    }
}
