package store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import model.AppointmentOutcomeRecord;
import observer.ISubscriber;
import observer.Notification;

/**
 * The AppointmentOutcomeRecordStore class is responsible for managing the storage and retrieval of Appointment Outcome Records.
 * It supports adding, removing, updating, and retrieving appointment outcome records and notifying subscribers of additions and removals.
 */
public class AppointmentOutcomeRecordStore {
    /**
     * A static variable to keep track of the next unique identifier for appointment outcome records.
     * It is initialized to 0 and increments each time a new appointment outcome record is added.
     */
    private static int nextId = 0;

    /**
     * A static HashMap to store appointment outcome records.
     * The key is the appointment outcome record ID and the value is the appointment outcome record.
     */
    private static HashMap<String, AppointmentOutcomeRecord> appointmentOutcomeRecords = new HashMap<String, AppointmentOutcomeRecord>();

    /**
     * A static HashSet to store subscribers that subscribe to appointment outcome record addition and removal notifications.
     */
    private static HashSet<ISubscriber> subscribers = new HashSet<ISubscriber>();

    /**
     * Adds a new appointment outcome record to the store and generates an appointment outcome record ID for the record.
     * The appointment outcome record ID is prefixed with "R" followed by a 6-digit number.
     * An addition notification will be sent to subscribers.
     *
     * @param record The appointment outcome record to be added
     * @return The unique identifier assigned to the appointment outcome record
     */
    public static String addRecord(AppointmentOutcomeRecord record) {
        String id = "R" + String.format("%06d", ++nextId);
        appointmentOutcomeRecords.put(id, record);
        record.setAppointmentOutcomeRecordId(id);
        Notification notification = new Notification("Appointment outcome record " + id + " has been added");
        notifySubscribers(notification);
        return id;
    }

    /**
     * Removes an appointment outcome record from the store based on the given ID.
     * A removal notification will be sent to subscribers.
     *
     * @param id The ID of the appointment outcome record to be removed
     */
    public static void removeRecord(String id) {
        appointmentOutcomeRecords.remove(id);
    }

    /**
     * Upserts the record of an appointment outcome record with the given ID.
     *
     * @param id The ID of the appointment outcome record to be upserted
     * @param record The new appointment outcome record
     */
    public static void updateRecord(String id, AppointmentOutcomeRecord record) {
        appointmentOutcomeRecords.put(id, record);
    }

    /**
     * Retrieves a list of all appointment outcome records.
     *
     * @return A list of the appointment outcome records
     */
    public static List<AppointmentOutcomeRecord> getRecords() {
        return new ArrayList<AppointmentOutcomeRecord>(appointmentOutcomeRecords.values());
    }

    /**
     * Retrieves an appointment outcome record by the provided appointment outcome record ID.
     *
     * @param id The appointment outcome record ID of the appointment outcome record to be retrieved
     * @return The appointment outcome record associated with the given ID, or null if no such appointment outcome record exists
     */
    public static AppointmentOutcomeRecord getRecord(String id) {
        return appointmentOutcomeRecords.get(id);
    }

    /**
     * Registers a subscriber to appointment outcome record addition and removal notifications.
     *
     * @param subscriber The subscriber to register
     */
    public static void subscribe(ISubscriber subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Unregisters a subscriber from appointment outcome record addition and removal notifications.
     *
     * @param subscriber The subscriber to unregister
     */
    public static void unsubscribe(ISubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    /**
     * Notifies all subscribers of an appointment outcome record addition or removal.
     *
     * @param notification The notification to be sent to subscribers
     */
    public static void notifySubscribers(Notification notification) {
        for (ISubscriber subscriber : subscribers) {
            subscriber.update(notification);
        }
    }
}
