package model;

import java.util.Date;

public class Appointment {
    private String appointmentId;
    private Patient patient;
    private Doctor doctor;
    private Date date;
    private AppointmentStatus status;
    private String outcomeRecordId;
    private boolean isRated;

    public Appointment (String appointmentId, Patient patient, Doctor doctor, Date date, AppointmentStatus status, String outcomeRecordId, boolean isRated) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.status = status;
        this.outcomeRecordId = outcomeRecordId;
        this.isRated = isRated;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Date getDate() {
        return date;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus newstatus) {
        this.status = newstatus;
    }

    public String getOutcomeRecordId() {
        return outcomeRecordId;
    }

    public void setOutcomeRecordId(String outcomeRecordId) {
        this.outcomeRecordId = outcomeRecordId;
    }

    public boolean getIsRated() {
        return isRated;
    }

    public void setIsRated(boolean isRated) {
        this.isRated = isRated;
    }
    
}
