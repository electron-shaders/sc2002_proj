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

/**
 * The PatientController class provides Patient with methods for scheduling, rescheduling, and cancelling appointments,
 * viewing scheduled and past appointments, searching for doctors, and printing bills and providing ratings for completed appointments.
 * <p>
 * It depends on Appointment, AppointmentOutcomeRecord, Bill, Doctor, Patient, AppointmentOutcomeRecordStore, AppointmentStore,
 * DoctorStore, and PatientStore.
 * </p>
 */
public class PatientController {
    /**
     * Gets the list of available appointment slots for the doctor with the specified user ID.
     *
     * @param doctorId the user ID of the doctor
     * @return the list of available appointment slots for the doctor
     * @throws Exception if the doctor is not found
     */
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

    /**
     * Schedules an appointment for a patient with a doctor on the specified date.
     *
     * @param patientId the user ID of the patient
     * @param doctorId  the user ID of the doctor
     * @param date      the date of the appointment
     * @return the scheduled PENDING appointment
     * @throws Exception if the patient is not found, the doctor is not found, or the doctor is not available on the specified date
     */
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

    /**
     * Reschedules an appointment for a patient to the new date.
     *
     * @param patientId     the user ID of the patient
     * @param appointmentId the ID of the appointment
     * @param newDate       the new date of the appointment
     * @return the rescheduled PENDING appointment
     * @throws Exception if the appointment is not found, the patient is not associated with the appointment, the appointment is not PENDING or CONFIRMED, or the doctor is not available on the new date
     */
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

    /**
     * Cancels an appointment for a patient.
     *
     * @param patientId     the user ID of the patient
     * @param appointmentId the ID of the appointment
     * @throws Exception if the appointment is not found, the patient is not associated with the appointment, or the appointment is not PENDING or CONFIRMED
     */
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

    /**
     * Gets the list of scheduled PENDING or CONFIRMED appointments for a patient.
     *
     * @param patientId the user ID of the patient
     * @return the list of scheduled appointments for the patient
     * @throws Exception if the patient is not found
     */
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

    /**
     * Gets the list of past COMPLETED or CANCELLED appointments for a patient.
     *
     * @param patientId the user ID of the patient
     * @return the list of past appointments for the patient
     * @throws Exception if the patient is not found
     */
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

    /**
     * Gets the appointment outcome record for a patient's COMPLETED appointment.
     *
     * @param patientId     the user ID of the patient
     * @param appointmentId the ID of the appointment
     * @return the appointment outcome record for the appointment
     * @throws Exception if the appointment is not found or the patient is not associated with the appointment
     */
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

    /**
     * Searches for doctors with the specified specialization.
     *
     * @param specialization the specialization of the doctors to search for
     * @return the list of doctors with the specified specialization
     */
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

    /**
     * Prints the bill for a patient's COMPLETED appointment.
     *
     * @param userId        the user ID of the patient
     * @param appointmentId the ID of the appointment
     * @return the printable bill for the appointment in String
     * @throws Exception if the appointment is not found, the patient is not associated with the appointment, or the appointment is not COMPLETED
     */
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

    /**
     * Provides the rating score for a patient's COMPLETED appointment.
     *
     * @param patientId     the user ID of the patient
     * @param appointmentId the ID of the appointment
     * @param rating        the rating score
     * @throws Exception if the rating is not between 1 and 5, the appointment is not found, the patient is not associated with the appointment, or the appointment is not COMPLETED or has already been rated
     */
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
