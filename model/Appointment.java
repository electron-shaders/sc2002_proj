package model;

import java.util.Date;
import java.util.HashSet;

import observer.IPublisher;
import observer.ISubscriber;
import observer.Notification;

public class Appointment implements IPublisher {
    private String appointmentId;
    private Patient patient;
    private Doctor doctor;
    private Date date;
    private AppointmentStatus status;
    private String outcomeRecordId;
    private boolean isRated;
    private HashSet<ISubscriber> subscribers;

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
        Notification notification = new Notification("Your Appointment " + appointmentId + " has been " + newstatus);
        notifySubscribers(notification);
    }

    public String getOutcomeRecordId() {
        return outcomeRecordId;
    }

    public void setOutcomeRecordId(String outcomeRecordId) {
        this.outcomeRecordId = outcomeRecordId;
        Notification notification = new Notification("Your Appointment " + appointmentId + " has an outcome record");
        notifySubscribers(notification);
    }

    public boolean getIsRated() {
        return isRated;
    }

    public void setIsRated(boolean isRated) {
        this.isRated = isRated;
    }
    
    public void subscribe(ISubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(ISubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void notifySubscribers(Notification notification) {
        for (ISubscriber subscriber : subscribers) {
            subscriber.update(notification);
        }
    }
}
