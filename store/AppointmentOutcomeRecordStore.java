package store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import model.AppointmentOutcomeRecord;
import observer.ISubscriber;
import observer.Notification;

public class AppointmentOutcomeRecordStore {
    private static int nextId = 0;
    private static HashMap<String, AppointmentOutcomeRecord> appointmentOutcomeRecords = new HashMap<String, AppointmentOutcomeRecord>();
    private static HashSet<ISubscriber> subscribers = new HashSet<ISubscriber>();

    public static String addRecord(AppointmentOutcomeRecord record) {
        String id = "R" + String.format("%06d", ++nextId);
        appointmentOutcomeRecords.put(id, record);
        record.setAppointmentOutcomeRecordId(id);
        Notification notification = new Notification("Appointment outcome record " + id + " has been added");
        notifySubscribers(notification);
        return id;
    }

    public static void removeRecord(String id) {
        appointmentOutcomeRecords.remove(id);
    }

    public static void updateRecord(String id, AppointmentOutcomeRecord record) {
        appointmentOutcomeRecords.put(id, record);
    }

    public static List<AppointmentOutcomeRecord> getRecords() {
        return new ArrayList<AppointmentOutcomeRecord>(appointmentOutcomeRecords.values());
    }

    public static AppointmentOutcomeRecord getRecord(String id) {
        return appointmentOutcomeRecords.get(id);
    }

    public static void subscribe(ISubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public static void unsubscribe(ISubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public static void notifySubscribers(Notification notification) {
        for (ISubscriber subscriber : subscribers) {
            subscriber.update(notification);
        }
    }
}
