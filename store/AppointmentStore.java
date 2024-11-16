package store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import model.Appointment;
import observer.ISubscriber;
import observer.Notification;

/**
 * The AppointmentStore class is responsible for managing the storage and retrieval of Appointment records.
 * It supports adding, removing, updating, and retrieving appointment records and notifying subscribers of additions and removals.
 */
public class AppointmentStore {
    /**
     * A static variable to keep track of the next unique identifier for appointments.
     * It is initialized to 0 and increments each time a new appointment is added.
     */
    private static int nextId = 0;

    /**
     * A static HashMap to store appointment records.
     * The key is the appointment ID and the value is the appointment record.
     */
    private static HashMap<String, Appointment> appointments = new HashMap<String, Appointment>();

    /**
     * A static HashSet to store subscribers that subscribe to appointment addition and removal notifications.
     */
    private static HashSet<ISubscriber> subscribers = new HashSet<ISubscriber>();

    /**
     * Adds a new appointment record to the store and generates an appointment ID for the record.
     * The appointment ID is prefixed with "AP" followed by a 6-digit number.
     * An addition notification will be sent to subscribers.
     *
     * @param record The appointment record to be added
     * @return The unique identifier assigned to the appointment
     */
    public static String addRecord(Appointment record) {
        String id = "AP" + String.format("%06d", ++nextId);
        appointments.put(id, record);
        record.setAppointmentId(id);
        Notification notification = new Notification("Appointment", id, "is added under", "Doctor", record.getDoctor().getUserId());
        notifySubscribers(notification);
        return id;
    }

    /**
     * Removes an appointment from the store based on the given ID.
     * A removal notification will be sent to subscribers.
     *
     * @param id The ID of the appointment record to be removed
     */
    public static void removeRecord(String id) {
        Appointment appointment = appointments.remove(id);
        if (appointment != null) {
            Notification notification = new Notification("Patient", appointment.getPatient().getUserId(), "removed", "Appointment", id);
            notifySubscribers(notification);
        }
    }

    /**
     * Upserts the record of an appointment with the given ID.
     *
     * @param id The ID of the appointment to be upserted
     * @param record The new appointment record
     */
    public static void updateRecord(String id, Appointment record) {
        appointments.put(id, record);
    }

    /**
     * Retrieves a list of all appointment records.
     *
     * @return A list of the appointment records
     */
    public static List<Appointment> getRecords() {
        return new ArrayList<Appointment>(appointments.values());
    }

    /**
     * Retrieves an appointment record by the provided appointment ID.
     *
     * @param id The appointment ID of the appointment to be retrieved
     * @return The appointment record associated with the given ID, or null if no such appointment exists
     */
    public static Appointment getRecord(String id) {
        return appointments.get(id);
    }

    /**
     * Registers a subscriber to appointment addition and removal notifications.
     *
     * @param subscriber The subscriber to register
     */
    public static void subscribe(ISubscriber subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Unregisters a subscriber from appointment addition and removal notifications.
     *
     * @param subscriber The subscriber to unregister
     */
    public static void unsubscribe(ISubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    /**
     * Notifies all subscribers of an appointment addition or removal.
     *
     * @param notification The notification to be sent to subscribers
     */
    public static void notifySubscribers(Notification notification) {
        for (ISubscriber subscriber : subscribers) {
            subscriber.update(notification);
        }
    }
}
