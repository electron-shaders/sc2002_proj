package model;

import java.util.Date;
import java.util.List;

public class AppointmentOutcomeRecord {
    String appointmentOutcomeRecordId;
    Date date;
    String serviceType;
    List<Prescription> prescriptions;
    String notes;

    public AppointmentOutcomeRecord(String appointmentOutcomeRecordId, Date date, String serviceType, List<Prescription> prescriptions, String notes) {
        this.appointmentOutcomeRecordId = appointmentOutcomeRecordId;
        this.date = date;
        this.serviceType = serviceType;
        this.prescriptions = prescriptions;
        this.notes = notes;
    }

    public String getAppointmentOutcomeRecordId() {
        return appointmentOutcomeRecordId;
    }

    public Date getDate () {
        return date;
    }

    public String getServiceType() {
        return serviceType;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptionStatus(int idx, PrescriptionStatus status) {
        prescriptions.get(idx).setStatus(status);
    }

    public String getNotes() {
        return notes;
    }
    
    
}
