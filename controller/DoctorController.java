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

public class DoctorController {
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

    public static void addPatientDiagnosis(String patientId, String diagnosis) throws Exception {
        Patient patient = PatientStore.getRecord(patientId);
        if (patient == null) {
            throw new Exception("Patient not found");
        }
        patient.addDiagnosis(diagnosis);
    }

    public static void addPatientPrescription(String patientId, String prescription) throws Exception {
        Patient patient = PatientStore.getRecord(patientId);
        if (patient == null) {
            throw new Exception("Patient not found");
        }
        patient.addPrescription(prescription);
    }

    public static void addPatientTreatment(String patientId, String treatment) throws Exception {
        Patient patient = PatientStore.getRecord(patientId);
        if (patient == null) {
            throw new Exception("Patient not found");
        }
        patient.addTreatment(treatment);
    }

    public static void acceptAppointment(String doctorId, String appointmentId) throws Exception {
        Appointment appointment = AppointmentStore.getRecord(appointmentId);
        if (appointment == null) {
            throw new Exception("Appointment not found");
        }

        if (!appointment.getDoctor().getUserId().equals(doctorId) || appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new Exception("Cannot accept appointment");
        }
        Doctor doctor = appointment.getDoctor();
        if (!doctor.isAvailable(appointment.getDate())) {
            throw new Exception("Doctor not available on this date");
        }

        doctor.removeAvailability(appointment.getDate());
        appointment.setStatus(AppointmentStatus.CONFIRMED);
    }

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
