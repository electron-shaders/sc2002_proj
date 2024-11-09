package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import store.AppointmentOutcomeRecordStore;
import store.AppointmentStore;
import store.DoctorStore;
import store.PatientStore;

public class PatientController {
    public static List<Date> getAppointmentSlots(String doctorId) {
        Doctor doctor = DoctorStore.getRecord(doctorId);
        if (doctor == null) {
            throw new Exception("Doctor not found");
        }
        return doctor.getAvailability();
    }

    public static void scheduleAppointment(String patientId, String doctorId, Date date) {
        Patient patient = PatientStore.getRecord(patientId);
        if (patient == null) {
            throw new Exception("Patient not found");
        }

        Doctor doctor = DoctorStore.getRecord(doctorId);
        if (doctor == null) {
            throw new Exception("Doctor not found");
        }

        if (!doctor.isAvailable(date)) {
            throw new Exception("Doctor not available on this date");
        }

        Appointment appointment = new Appointment(null, patient, doctor, date, AppointmentStatus.PENDING, null, false);
        AppointmentStore.addRecord(appointment);
    }

    public static void rescheduleAppointment(String patientId, String appointmentId, Date newDate) {
        Appointment appointment = AppointmentStore.getRecord(appointmentId);
        if (appointment == null) {
            throw new Exception("Appointment not found");
        }

        if (!appointment.getPatient.getId().equals(patientId) || appointment.getStatus() != AppointmentStatus.PENDING && appointment.getStatus() != AppointmentStatus.COFIRMED) {
            throw new Exception("Cannot reschedule appointment");
        }

        Doctor doctor = appointment.getDoctor();
        if (!doctor.isAvailable(newDate)) {
            throw new Exception("Doctor not available on this date");
        }
        AppointmentStore.removeRecord(appointmentId);

        doctor.removeAvailability(appointment.getDate());
        doctor.addAvailability(newDate);
        Appointment newAppointment = new Appointment(null, appointment.getPatient(), doctor, newDate, AppointmentStatus.PENDING, null, false);
        AppointmentStore.addRecord(newAppointment);
    }

    public static void cancelAppointment(String patientId, String appointmentId) {
        Appointment appointment = AppointmentStore.getRecord(appointmentId);
        if (appointment == null) {
            throw new Exception("Appointment not found");
        }

        if (!appointment.getPatient().getId().equals(patientId) || appointment.getStatus() != AppointmentStatus.PENDING && appointment.getStatus() != AppointmentStatus.COFIRMED) {
            throw new Exception("Cannot cancel appointment");
        }
        Doctor doctor = appointment.getDoctor();
        doctor.addAvailability(appointment.getDate());
        appointment.setStatus(AppointmentStatus.CANCELLED);
    }

    public static List<Appointment> getScheduledAppointments(String patientId) {
        Patient patient = PatientStore.getRecord(patientId);
        if (patient == null) {
            throw new Exception("Patient not found");
        }

        List<Appointment> appointmentList = AppointmentStore.getRecords();
        List<Appointment> patientAppointments = new ArrayList<Appointment>();
        for (Appointment appointment : appointmentList) {
            if (appointment.getPatient().getId().equals(patientId) && (appointment.getStatus() == AppointmentStatus.PENDING || appointment.getStatus() == AppointmentStatus.COFIRMED)) {
                patientAppointments.add(appointment);
            }
        }

        return patientAppointments;
    }

    public static List<Appointment> getPastAppointments(String patientId) {
        Patient patient = PatientStore.getRecord(patientId);
        if (patient == null) {
            throw new Exception("Patient not found");
        }

        List<Appointment> appointmentList = AppointmentStore.getRecords();
        List<Appointment> patientAppointments = new ArrayList<Appointment>();
        for (Appointment appointment : appointmentList) {
            if (appointment.getPatient().getId().equals(patientId) && (appointment.getStatus() == AppointmentStatus.COMPLETED || appointment.getStatus() == AppointmentStatus.CANCELLED)) {
                patientAppointments.add(appointment);
            }
        }

        return patientAppointments;
    }

    public static AppointmentOutcomeRecord getAppointmentOutcomeRecord(String patientId, String appointmentId) {
        Appointment appointment = AppointmentStore.getRecord(appointmentId);
        if (appointment == null || !appointment.getPatient().getId().equals(patientId)) {
            throw new Exception("Appointment not found");
        }

        String outcomeRecordId = appointment.getOutcomeRecordId();
        if (outcomeRecordId == null) {
            throw new Exception("Outcome record not found");
        }

        return AppointmentOutcomeRecordStore.getRecord(outcomeRecordId);
    }

    public static List<Doctor> searchDoctor(String specialization) {
        List<Doctor> doctorList = DoctorStore.getRecords();
        List<Doctor> filteredDoctors = new ArrayList<Doctor>();
        for (Doctor doctor : doctorList) {
            if (doctor.getSpecialty().equals(specialization)) {
                filteredDoctors.add(doctor);
            }
        }

        Collections.sort(filteredDoctors);
        return filteredDoctors;
    }

    public static float calculateBill(Bill bill) {}

    public static void printBill(Bill bill) {}

    public static void provideRating(String patientId, String appointmentId, int rating) {
        if (rating < 1 || rating > 5) {
            throw new Exception("Invalid rating, rating should be between 1 and 5");
        }

        Appointment appointment = AppointmentStore.getRecord(appointmentId);
        if (appointment == null) {
            throw new Exception("Appointment not found");
        }

        if (!appointment.getPatient().getId().equals(patientId) || appointment.getStatus() != AppointmentStatus.COMPLETED || appointment.getIsRated()) {
            throw new Exception("Cannot rate appointment");
        }

        Doctor doctor = appointment.getDoctor();
        int ratingCount = doctor.getRatingCount();
        int oldRating = doctor.getRating();
        int newRating = (oldRating * ratingCount + rating) / (ratingCount + 1);
        doctor.setRating(newRating);
        doctor.incrementRatingCount();
        appointment.setIsRated(true);
    }
}
