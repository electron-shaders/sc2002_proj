package model;

import java.util.Date;
import java.util.List;

/**
 * The AppointmentOutcomeRecord class represents an appointment outcome record, which is part of a Bill and an Appointment.
 * It contains information about the outcome of a patient's appointment, like the date of the appointment and consultation notes.
 */
public class AppointmentOutcomeRecord {
    /**
     * The ID of the appointment outcome record.
     */
    private String appointmentOutcomeRecordId;

    /**
     * The appointment date of the appointment outcome record.
     */
    private Date date;

    /**
     * The service type of the appointment outcome record.
     */
    private String serviceType;

    /**
     * The list of prescriptions in the appointment outcome record.
     */
    private List<Prescription> prescriptions;

    /**
     * The consultation notes of the appointment outcome record.
     */
    private String notes;

    /**
     * Constructs a new AppointmentOutcomeRecord with the specified details.
     *
     * @param appointmentOutcomeRecordId the ID of the appointment outcome record
     * @param date                       the appointment date of the appointment outcome record
     * @param serviceType                the service type of the appointment outcome record
     * @param prescriptions              the list of prescriptions in the appointment outcome record
     * @param notes                      the consultation notes of the appointment outcome record
     */
    public AppointmentOutcomeRecord(String appointmentOutcomeRecordId, Date date, String serviceType, List<Prescription> prescriptions, String notes) {
        this.appointmentOutcomeRecordId = appointmentOutcomeRecordId;
        this.date = date;
        this.serviceType = serviceType;
        this.prescriptions = prescriptions;
        this.notes = notes;
    }

    /**
     * Getter for the ID of the appointment outcome record.
     * @return the ID of the appointment outcome record
     */
    public String getAppointmentOutcomeRecordId() {
        return appointmentOutcomeRecordId;
    }

    /**
     * Setter for the ID of the appointment outcome record.
     * @param appointmentOutcomeRecordId the new ID of the appointment outcome record
     */
    public void setAppointmentOutcomeRecordId(String appointmentOutcomeRecordId) {
        this.appointmentOutcomeRecordId = appointmentOutcomeRecordId;
    }

    /**
     * Getter for the appointment date of the appointment outcome record.
     * @return the appointment date of the appointment outcome record
     */
    public Date getDate () {
        return date;
    }

    /**
     * Getter for the service type of the appointment outcome record.
     * @return the service type of the appointment outcome record
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Getter for the list of prescriptions in the appointment outcome record.
     * @return the list of prescriptions in the appointment outcome record
     */
    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    /**
     * Updates the prescription status of a prescription in the appointment outcome record.
     * @param idx the index of the prescription in the list of prescriptions of the appointment outcome record
     * @param status the new status of the prescription
     */
    public void setPrescriptionStatus(int idx, PrescriptionStatus status) {
        prescriptions.get(idx).setStatus(status);
    }

    /**
     * Getter for the consultation notes of the appointment outcome record.
     * @return the consultation notes of the appointment outcome record
     */
    public String getNotes() {
        return notes;
    }
}
