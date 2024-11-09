package controller;

import java.util.ArrayList;
import java.util.List;

import store.AppointmentOutcomeRecordStore;
import store.AppointmentStore;
import store.PatientStore;

public class DoctorController {
    public static List<Patient> getPatientsUnderCare(String doctorId) {
        List<Patient> patientList = PatientStore.getRecords();
        List<Patient> patientsUnderCare = new ArrayList<Patient>();
        for (Patient patient : patientList) {
            if (patient.getDoctor().getId().equals(doctorId)) {
                patientsUnderCare.add(patient);
            }
        }
        
        return patientsUnderCare;
    }

    public static void addPatientDiagnosis(String patientId, String diagnosis) {
        Patient patient = PatientStore.getRecord(patientId);
        if (patient == null) {
            throw new Exception("Patient not found");
        }
        patient.addDiagnosis(diagnosis);
    }

    public static void addPatientPrescription(String patientId, String prescription) {
        Patient patient = PatientStore.getRecord(patientId);
        if (patient == null) {
            throw new Exception("Patient not found");
        }
        patient.addPrescription(prescription);
    }

    public static void addPatientTreatment(String patientId, String treatment) {
        Patient patient = PatientStore.getRecord(patientId);
        if (patient == null) {
            throw new Exception("Patient not found");
        }
        patient.addTreatment(treatment);
    }

    public static void acceptAppointment(String doctorId, String appointmentId) {
        Appointment appointment = AppointmentStore.getRecord(appointmentId);
        if (appointment == null) {
            throw new Exception("Appointment not found");
        }

        if (!appointment.getDoctor().getId().equals(doctorId) || appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new Exception("Cannot accept appointment");
        }
        Doctor doctor = appointment.getDoctor();
        if (!doctor.isAvailable(appointment.getDate())) {
            throw new Exception("Doctor not available on this date");
        }

        doctor.removeAvailability(appointment.getDate());
        appointment.setStatus(AppointmentStatus.ACCEPTED);
    }

    public static void declineAppointment(String doctorId, String appointmentId) {
        Appointment appointment = AppointmentStore.getRecord(appointmentId);
        if (appointment == null) {
            throw new Exception("Appointment not found");
        }

        if (!appointment.getDoctor().getId().equals(doctorId) || appointment.getStatus() != AppointmentStatus.PENDING) {
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
            if (appointment.getDoctor().getId().equals(doctorId) && (appointment.getStatus() == AppointmentStatus.PENDING || appointment.getStatus() == AppointmentStatus.CONFIRMED)) {
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

    public static void recordAppointmentOutcome(String doctorId, String appointmentId, String serviceType, List<Presciption> prescription, String notes) {
        Appointment appointment = AppointmentStore.getRecord(appointmentId);
        if (appointment == null) {
            throw new Exception("Appointment not found");
        }

        if (!appointment.getDoctor().getId().equals(doctorId) || appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new Exception("Cannot record outcome for this appointment");
        }
        AppointmentOutcomeRecord outcomeRecord = new AppointmentOutcomeRecord(null, appointment.getDate(), serviceType, prescription, notes);
        String outcomeRecordId = AppointmentOutcomeRecordStore.addRecord(outcomeRecord);
        appointment.setOutcomeRecordId(outcomeRecordId);
        appointment.setStatus(AppointmentStatus.COMPLETED);
    }
}
