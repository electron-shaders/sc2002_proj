package controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import model.Appointment;
import model.AppointmentOutcomeRecord;
import model.AppointmentStatus;
import model.Doctor;
import model.Medicine;
import model.Patient;
import model.Prescription;
import store.AppointmentOutcomeRecordStore;
import store.AppointmentStore;
import store.MedicineStore;
import store.PatientStore;

/**
 * The DoctorController class provides Doctor with methods for managing medical records of patients under a doctor's care,
 * accepting or declining appointments, searching for medicines, and recording appointment outcomes.
 * <p>
 * It depends on Appointment, AppointmentOutcomeRecord, Doctor, Medicine, Patient, Prescription,
 * AppointmentOutcomeRecordStore, AppointmentStore, MedicineStore, and PatientStore.
 * </p>
 */
public class DoctorController {
    /**
     * Gets the list of patients under the care of the doctor with the specified user ID.
     *
     * @param doctorId the user ID of the doctor
     * @return the list of patients under the care of the doctor
     */
    public static List<Patient> getPatientsUnderCare(String doctorId) {
        List<Appointment> appointmentList = AppointmentStore.getRecords();
        HashSet<Patient> patientsUnderCare = new HashSet<Patient>();
        for (Appointment appointment : appointmentList) {
            if (appointment.getDoctor().getUserId().equals(doctorId)) {
                patientsUnderCare.add(appointment.getPatient());
            }
        }
        
        return new ArrayList<Patient>(patientsUnderCare);
    }

    /**
     * Adds a diagnosis to the patient with the specified user ID.
     *
     * @param patientId the user ID of the patient
     * @param diagnosis the diagnosis to add
     * @throws Exception if the patient is not found
     */
    public static void addPatientDiagnosis(String patientId, String diagnosis) throws Exception {
        Patient patient = PatientStore.getRecord(patientId);
        if (patient == null) {
            throw new Exception("Patient not found");
        }
        patient.addDiagnosis(diagnosis);
    }

    /**
     * Adds a prescription to the patient with the specified user ID.
     *
     * @param patientId    the user ID of the patient
     * @param prescription the prescription to add
     * @throws Exception if the patient is not found
     */
    public static void addPatientPrescription(String patientId, String prescription) throws Exception {
        Patient patient = PatientStore.getRecord(patientId);
        if (patient == null) {
            throw new Exception("Patient not found");
        }
        patient.addPrescription(prescription);
    }

    /**
     * Adds a treatment to the patient with the specified user ID.
     *
     * @param patientId the user ID of the patient
     * @param treatment the treatment to add
     * @throws Exception if the patient is not found
     */
    public static void addPatientTreatment(String patientId, String treatment) throws Exception {
        Patient patient = PatientStore.getRecord(patientId);
        if (patient == null) {
            throw new Exception("Patient not found");
        }
        patient.addTreatment(treatment);
    }

    /**
     * Accepts an appointment with the specified appointment ID.
     *
     * @param doctorId      the user ID of the doctor
     * @param appointmentId the ID of the appointment
     * @throws Exception if the appointment is not found or is not PENDING
     */
    public static void acceptAppointment(String doctorId, String appointmentId) throws Exception {
        Appointment appointment = AppointmentStore.getRecord(appointmentId);
        if (appointment == null) {
            throw new Exception("Appointment not found");
        }

        if (!appointment.getDoctor().getUserId().equals(doctorId) || appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new Exception("Cannot accept appointment");
        }
        appointment.setStatus(AppointmentStatus.CONFIRMED);
    }

    /**
     * Declines an appointment with the specified appointment ID.
     *
     * @param doctorId      the user ID of the doctor
     * @param appointmentId the ID of the appointment
     * @throws Exception if the appointment is not found or is not PENDING
     */
    public static void declineAppointment(String doctorId, String appointmentId) throws Exception {
        Appointment appointment = AppointmentStore.getRecord(appointmentId);
        if (appointment == null) {
            throw new Exception("Appointment not found");
        }

        if (!appointment.getDoctor().getUserId().equals(doctorId) || appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new Exception("Cannot decline appointment");
        }
        Doctor doctor = appointment.getDoctor();
        doctor.addAvailability(appointment.getDate());
        appointment.setStatus(AppointmentStatus.CANCELLED);
    }

    /**
     * Gets the list of upcoming appointments for the doctor with the specified user ID.
     *
     * @param doctorId the user ID of the doctor
     * @return the list of upcoming appointments for the doctor
     */
    public static List<Appointment> getUpcomingAppointments(String doctorId) {
        List<Appointment> appointmentList = AppointmentStore.getRecords();
        List<Appointment> upcomingAppointments = new ArrayList<Appointment>();
        for (Appointment appointment : appointmentList) {
            if (appointment.getDoctor().getUserId().equals(doctorId) && (appointment.getStatus() == AppointmentStatus.PENDING || appointment.getStatus() == AppointmentStatus.CONFIRMED)) {
                upcomingAppointments.add(appointment);
            }
        }
        
        return upcomingAppointments;
    }

    /**
     * Searches for a medicine with the specified name. The search is case-insensitive.
     *
     * @param name the name of the medicine to search for
     * @return the medicine with the specified name, or null if not found
     */
    public static Medicine findMedicine(String name) {
        name = name.toLowerCase();
        List<Medicine> medicineList = MedicineStore.getRecords();
        for (Medicine medicine : medicineList) {
            if (medicine.getName().toLowerCase().equals(name)) {
                return medicine;
            }
        }
        return null;
    }

    /**
     * Records the outcome of an appointment with the specified appointment ID.
     *
     * @param doctorId      the user ID of the doctor
     * @param appointmentId the ID of the appointment
     * @param serviceType   the service type of the appointment
     * @param prescription  the list of prescriptions in the appointment outcome
     * @param notes         the consultation notes of the appointment outcome
     * @throws Exception if the appointment is not found, is not CONFIRMED, or the doctor does not match
     */
    public static void recordAppointmentOutcome(String doctorId, String appointmentId, String serviceType, List<Prescription> prescription, String notes) throws Exception {
        Appointment appointment = AppointmentStore.getRecord(appointmentId);
        if (appointment == null) {
            throw new Exception("Appointment not found");
        }

        if (!appointment.getDoctor().getUserId().equals(doctorId) || appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new Exception("Cannot record outcome for this appointment");
        }
        AppointmentOutcomeRecord outcomeRecord = new AppointmentOutcomeRecord(null, appointment.getDate(), serviceType, prescription, notes);
        String outcomeRecordId = AppointmentOutcomeRecordStore.addRecord(outcomeRecord);
        appointment.setOutcomeRecordId(outcomeRecordId);
        appointment.setStatus(AppointmentStatus.COMPLETED);
    }
}
