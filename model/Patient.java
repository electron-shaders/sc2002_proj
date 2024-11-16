package model;

import java.util.Date;

/**
 * The Patient class represents a patient in the system.
 * It is a User and contains patient-specific information like the medical record.
 */
public class Patient extends User {
    /**
     * The date of birth of the patient.
     */
    private Date dateofBirth;

    /**
     * The blood type of the patient.
     */
    private String bloodType;

    /**
     * The medical record of the patient.
     */
    private MedicalRecord medicalRecord;

    /**
     * Constructs a new Patient with the specified details.
     *
     * @param userId        the user ID for the patient
     * @param password      the password for the patient
     * @param role          the role of the patient (PATIENT)
     * @param name          the name of the patient
     * @param isMale        the gender of the user, true for Male, false for Female
     * @param email         the email address of the user
     * @param dateofBirth   the date of birth of the patient
     * @param bloodType     the blood type of the patient
     * @param medicalRecord the medical record of the patient
     * @see User
     */
    public Patient(String userId, String password, UserRole role, String name, boolean isMale, String email, Date dateofBirth, String bloodType, MedicalRecord medicalRecord) {
        super(userId, password, role, name, isMale, 0, email);
        this.dateofBirth = dateofBirth;
        this.bloodType = bloodType;
        this.medicalRecord = medicalRecord;
    }

    /**
     * Getter for the date of birth of the patient.
     * @return the date of birth of the patient
     */
    public Date getDateOfBirth() {
        return dateofBirth;
    }

    /**
     * Setter for the date of birth of the patient.
     * @param dateofBirth the new date of birth of the patient
     */
    public void setDateOfBirth(Date dateofBirth) {
        this.dateofBirth = dateofBirth;
    }

    /**
     * Getter for the blood type of the patient.
     * @return the blood type of the patient
     */
    public String getBloodType() {
        return bloodType;
    }

    /**
     * Getter for the medical record of the patient.
     * @return the medical record of the patient
     */
    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    /**
     * Adds a prescription to the patient's medical record.
     * @param prescription the prescription to be added
     */
    public void addPrescription(String prescription) {
        this.medicalRecord.addPrescription(prescription);
    }

    /**
     * Adds a diagnosis to the patient's medical record.
     * @param diagnosis the diagnosis to be added
     */
    public void addDiagnosis(String diagnosis) {
        this.medicalRecord.addDiagnosis(diagnosis);
    }

    /**
     * Adds a treatment to the patient's medical record.
     * @param treatment the treatment to be added
     */
    public void addTreatment(String treatment) {
        this.medicalRecord.addTreatment(treatment);
    }
}
