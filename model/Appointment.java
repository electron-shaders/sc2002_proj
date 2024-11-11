package model;

import java.util.Date;

public class Appointment {
    private String appointmentId;
    private Patient patient;
    private Doctor doctor;
    private Date date;
    private AppointmentStatus status;
    private String outcomeRecordId;

    public Appointment (String appointmentId, Patient patient, Doctor doctor, Date date, AppointmentStatus status, String outcomeRecordId) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.status = status;
        this.outcomeRecordId = outcomeRecordId;
    }

    public String getAppointmentId() {
        return appointmentId;
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

    public String getoutcomeRecordId() {
        return outcomeRecordId;
    }

    public void setOutcomeRecordId(String outcomeRecordId) {
        this.outcomeRecordId = outcomeRecordId;
    }

    public String getAppointmentDetails() {
        return "Appointment ID: " + appointmentId + "\nPatient: " + patient + "\nDate: " + date + "\nStatus: " + status + "\nOutcome Record ID: " + outcomeRecordId;
    }
}
