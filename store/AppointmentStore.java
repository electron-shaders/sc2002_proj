package store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import model.Appointment;
import observer.ISubscriber;
import observer.Notification;

public class AppointmentStore {
    private static int nextId = 0;
    private static HashMap<String, Appointment> appointments = new HashMap<String, Appointment>();
    private static HashSet<ISubscriber> subscribers = new HashSet<ISubscriber>();

    public static String addRecord(Appointment record) {
        String id = "AP" + String.format("%06d", ++nextId);
        appointments.put(id, record);
        record.setAppointmentId(id);
        Notification notification = new Notification("Appointment", id, "is added under", "Doctor", record.getDoctor().getUserId());
        notifySubscribers(notification);
        return id;
    }

    public static void removeRecord(String id) {
        Appointment appointment = appointments.remove(id);
        if (appointment != null) {
            Notification notification = new Notification("Patient", appointment.getPatient().getUserId(), "removed", "Appointment", id);
            notifySubscribers(notification);
        }
    }

    public static void updateRecord(String id, Appointment record) {
        appointments.put(id, record);
    }

    public static List<Appointment> getRecords() {
        return new ArrayList<Appointment>(appointments.values());
    }

    public static Appointment getRecord(String id) {
        return appointments.get(id);
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
