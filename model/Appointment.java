package model;

import java.util.Date;
import java.util.HashSet;

import observer.IPublisher;
import observer.ISubscriber;
import observer.Notification;

/**
 * The Appointment class represents an appointment, which associates a Patient with a Doctor.
 * It is a Publisher and notifies Subscribers when the status of the appointment changes.
 */
public class Appointment implements IPublisher {
    /**
     * The ID of the appointment.
     */
    private String appointmentId;

    /**
     * The patient of the appointment.
     */
    private Patient patient;

    /**
     * The doctor of the appointment.
     */
    private Doctor doctor;

    /**
     * The date of the appointment.
     */
    private Date date;

    /**
     * The status of the appointment.
     */
    private AppointmentStatus status;

    /**
     * The ID of the outcome record of the appointment.
     */
    private String outcomeRecordId;

    /**
     * Whether the appointment has been rated.
     */
    private boolean isRated;

    /**
     * The set of subscribers to the appointment.
     */
    private HashSet<ISubscriber> subscribers;

    /**
     * Constructs a new Appointment with the specified details.
     *
     * @param appointmentId    the ID of the appointment
     * @param patient          the patient of the appointment
     * @param doctor           the doctor of the appointment
     * @param date             the date of the appointment
     * @param status           the status of the appointment
     * @param outcomeRecordId  the ID of the outcome record of the appointment
     * @param isRated          whether the appointment has been rated
     */
    public Appointment (String appointmentId, Patient patient, Doctor doctor, Date date, AppointmentStatus status, String outcomeRecordId, boolean isRated) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.status = status;
        this.outcomeRecordId = outcomeRecordId;
        this.isRated = isRated;
        this.subscribers = new HashSet<ISubscriber>();
    }

    /**
     * Getter for the ID of the appointment.
     * @return the ID of the appointment
     */
    public String getAppointmentId() {
        return appointmentId;
    }

    /**
     * Setter for the ID of the appointment.
     * @param appointmentId the new ID of the appointment
     */
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Getter for the Patient of the appointment.
     * @return the Patient of the appointment
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Getter for the Doctor of the appointment.
     * @return the Doctor of the appointment
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Getter for the date of the appointment.
     * @return the date of the appointment
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for the status of the appointment.
     * @return the status of the appointment
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * Setter for the status of the appointment.
     * Notifies subscribers when the status changes.
     * @param newstatus the new status of the appointment
     */
    public void setStatus(AppointmentStatus newstatus) {
        this.status = newstatus;
        Notification notification = new Notification("Your Appointment " + appointmentId + " has been " + newstatus);
        notifySubscribers(notification);
    }

    /**
     * Getter for the ID of the outcome record of the appointment.
     * @return the ID of the outcome record of the appointment
     */
    public String getOutcomeRecordId() {
        return outcomeRecordId;
    }

    /**
     * Setter for the ID of the outcome record of the appointment.
     * Notifies subscribers when an outcome record is added.
     * @param outcomeRecordId the new ID of the outcome record of the appointment
     */
    public void setOutcomeRecordId(String outcomeRecordId) {
        this.outcomeRecordId = outcomeRecordId;
        Notification notification = new Notification("Your Appointment " + appointmentId + " has an outcome record");
        notifySubscribers(notification);
    }

    /**
     * Getter for whether the appointment has been rated.
     * @return whether the appointment has been rated
     */
    public boolean getIsRated() {
        return isRated;
    }

    /**
     * Setter for whether the appointment has been rated.
     * @param isRated whether the appointment has been rated
     */
    public void setIsRated(boolean isRated) {
        this.isRated = isRated;
    }

    /**
     * Registers a subscriber to the appointment status change or outcome record creation notifications.
     * @param subscriber the subscriber to register
     */
    public void subscribe(ISubscriber subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Unregisters a subscriber from the appointment status change or outcome record creation notifications.
     * @param subscriber the subscriber to unregister
     */
    public void unsubscribe(ISubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    /**
     * Notifies all subscribers of the appointment status change or outcome record creation.
     * @param notification the notification to send to subscribers
     */
    public void notifySubscribers(Notification notification) {
        for (ISubscriber subscriber : subscribers) {
            subscriber.update(notification);
        }
    }
}
