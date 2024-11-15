package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import model.Appointment;
import model.AppointmentOutcomeRecord;
import model.AppointmentStatus;
import model.Bill;
import model.Doctor;
import model.Patient;
import store.AppointmentOutcomeRecordStore;
import store.AppointmentStore;
import store.DoctorStore;
import store.PatientStore;

public class PatientController {
    public static List<Date> getAppointmentSlots(String doctorId) throws Exception {
        Doctor doctor = DoctorStore.getRecord(doctorId);
        if (doctor == null) {
            throw new Exception("Doctor not found");
        }
        Map<Date, Boolean> availability = doctor.getAvailability();
        List<Date> availableSlots = new ArrayList<Date>();
        for (Map.Entry<Date, Boolean> entry : availability.entrySet()) {
            if (entry.getValue()) {
                availableSlots.add(entry.getKey());
            }
        }
        return availableSlots;
    }

    public static Appointment scheduleAppointment(String patientId, String doctorId, Date date) throws Exception {
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
        doctor.removeAvailability(date);
        Appointment appointment = new Appointment(null, patient, doctor, date, AppointmentStatus.PENDING, null, false);
        AppointmentStore.addRecord(appointment);
        return appointment;
    }

    public static Appointment rescheduleAppointment(String patientId, String appointmentId, Date newDate) throws Exception {
        Appointment appointment = AppointmentStore.getRecord(appointmentId);
        if (appointment == null) {
            throw new Exception("Appointment not found");
        }

        if (!appointment.getPatient().getUserId().equals(patientId) || appointment.getStatus() != AppointmentStatus.PENDING && appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new Exception("Cannot reschedule appointment");
        }

        Doctor doctor = appointment.getDoctor();
        if (!doctor.isAvailable(newDate)) {
            throw new Exception("Doctor not available on this date");
        }
        AppointmentStore.removeRecord(appointmentId);

        doctor.removeAvailability(newDate);
        doctor.addAvailability(appointment.getDate());
        Appointment newAppointment = new Appointment(null, appointment.getPatient(), doctor, newDate, AppointmentStatus.PENDING, null, false);
        AppointmentStore.addRecord(newAppointment);
        return newAppointment;
    }

    public static void cancelAppointment(String patientId, String appointmentId) throws Exception {
        Appointment appointment = AppointmentStore.getRecord(appointmentId);
        if (appointment == null) {
            throw new Exception("Appointment not found");
        }

        if (!appointment.getPatient().getUserId().equals(patientId) || appointment.getStatus() != AppointmentStatus.PENDING && appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new Exception("Cannot cancel appointment");
        }
        Doctor doctor = appointment.getDoctor();
        doctor.addAvailability(appointment.getDate());
        appointment.setStatus(AppointmentStatus.CANCELLED);
    }

    public static List<Appointment> getScheduledAppointments(String patientId) throws Exception {
        Patient patient = PatientStore.getRecord(patientId);
        if (patient == null) {
            throw new Exception("Patient not found");
        }

        List<Appointment> appointmentList = AppointmentStore.getRecords();
        List<Appointment> patientAppointments = new ArrayList<Appointment>();
        for (Appointment appointment : appointmentList) {
            if (appointment.getPatient().getUserId().equals(patientId) && (appointment.getStatus() == AppointmentStatus.PENDING || appointment.getStatus() == AppointmentStatus.CONFIRMED)) {
                patientAppointments.add(appointment);
            }
        }

        return patientAppointments;
    }

    public static List<Appointment> getPastAppointments(String patientId) throws Exception {
        Patient patient = PatientStore.getRecord(patientId);
        if (patient == null) {
            throw new Exception("Patient not found");
        }

        List<Appointment> appointmentList = AppointmentStore.getRecords();
        List<Appointment> patientAppointments = new ArrayList<Appointment>();
        for (Appointment appointment : appointmentList) {
            if (appointment.getPatient().getUserId().equals(patientId) && (appointment.getStatus() == AppointmentStatus.COMPLETED || appointment.getStatus() == AppointmentStatus.CANCELLED)) {
                patientAppointments.add(appointment);
            }
        }

        return patientAppointments;
    }

    public static AppointmentOutcomeRecord getAppointmentOutcomeRecord(String patientId, String appointmentId) throws Exception {
        Appointment appointment = AppointmentStore.getRecord(appointmentId);
        if (appointment == null || !appointment.getPatient().getUserId().equals(patientId)) {
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
        if (specialization == null || specialization.isEmpty()) {
            Collections.sort(doctorList);
            return doctorList;
        }

        List<Doctor> filteredDoctors = new ArrayList<Doctor>();
        for (Doctor doctor : doctorList) {
            if (doctor.getSpecialty().equals(specialization)) {
                filteredDoctors.add(doctor);
            }
        }

        Collections.sort(filteredDoctors);
        return filteredDoctors;
    }

    public static String printBill(String userId, String appointmentId) throws Exception {
        Appointment appointment = AppointmentStore.getRecord(appointmentId);
        if (appointment == null || !appointment.getPatient().getUserId().equals(userId)) {
            throw new Exception("Appointment not found");
        }
        String appointmentOutcomeRecordId = appointment.getOutcomeRecordId();
        if (appointment.getStatus() != AppointmentStatus.COMPLETED || appointmentOutcomeRecordId == null) {
            throw new Exception("Cannot print bill for this appointment");
        }
        AppointmentOutcomeRecord outcomeRecord = AppointmentOutcomeRecordStore.getRecord(appointmentOutcomeRecordId);
        if (outcomeRecord == null) {
            throw new Exception("Cannot print bill for this appointment");
        }
        Bill bill = new Bill(outcomeRecord);
        return bill.toString();
    }

    public static void provideRating(String patientId, String appointmentId, int rating) throws Exception {
        if (rating < 1 || rating > 5) {
            throw new Exception("Invalid rating, rating should be between 1 and 5");
        }

        Appointment appointment = AppointmentStore.getRecord(appointmentId);
        if (appointment == null) {
            throw new Exception("Appointment not found");
        }

        if (!appointment.getPatient().getUserId().equals(patientId) || appointment.getStatus() != AppointmentStatus.COMPLETED || appointment.getIsRated()) {
            throw new Exception("Cannot rate appointment");
        }

        Doctor doctor = appointment.getDoctor();
        int ratingCount = doctor.getRatingCount();
        float oldRating = doctor.getRating();
        float newRating = (oldRating * ratingCount + rating) / (ratingCount + 1);
        doctor.setRating(newRating);
        doctor.incrementRatingCount();
        appointment.setIsRated(true);
    }
}
