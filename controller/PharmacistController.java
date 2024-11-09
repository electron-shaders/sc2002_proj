import java.util.List;

import store.AppointmentOutcomeRecordStore;
import store.MedicineStore;

public class PharmacistController {
    public AppointmentOutcomeRecord getAppointmentOutcomeRecord(String appointmentOutcomeRecordedId){
        AppointmentOutcomeRecord OutcomeRecord =  AppointmentOutcomeRecordStore.getRecord(appointmentOutcomeRecordedId);
        if(OutcomeRecord != null){
            return OutcomeRecord;
        }
        throw new Exception("Outcome Record is not found.");
    }
    public void approvePrescriptionRequest(String appointmentOutcomeRecordId, int prescriptionIdx){
        AppointmentOutcomeRecord Record = AppointmentOutcomeRecordStore.getRecord(appointmentOutcomeRecordedId);
        if(Record == null){
            throw new Exception("Outcome Record is not found.");
        }
        List<Presciption> prescription = Record.getPrescriptions();
        if(prescriptionIdx < 0 || prescriptionIdx >= prescription.length){
            throw new Exception("Invalid prescription Id.");
        }
        Medicine medicine = prescription.get(prescriptionIdx);
        medicine.setStock(medicine.getStock() - 1);
        medicine.setStatus(PrescriptionStatus.DISPENSED);
    }
    public List<Medicine> getMedicineInventory(){
        return MedicineStore.getRecords();
    }
    public void submitReplenishmentRequest(String medicinedId){
        Medicine medicine = MedicineStore.getRecord(medicinedId);
        if(medicine != null && medicine.getStock() <= medicine.getLowStockThreshold()){
            medicine.setIsRequestingReplenishment(true);
            return;
        }
        throw new Exception("Cannot sumbmit medicine replenishment request.");
    }
}
