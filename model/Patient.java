package model;

import java.util.Date;

public class Patient extends User {
    private Date dateofBirth;
    private String bloodType;
    private MedicalRecord medicalRecord;

    public Patient(String userId, String password, UserRole role, String name, boolean isMale, String email, Date dateofBirth, String bloodType, MedicalRecord medicalRecord) {
        // constructor logic
        // age is not needed for patients
        super(userId, password, role, name, isMale, 0, email);
        this.dateofBirth = dateofBirth;
        this.bloodType = bloodType;
        this.medicalRecord = medicalRecord;
    }

    public Date getDateOfBirth() {
        return dateofBirth;
    }

    public void setDateOfBirth(Date dateofBirth) {
        this.dateofBirth = dateofBirth;
    }

    public String getBloodType() {
        return bloodType;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void addPrescription(String prescription) {
        this.medicalRecord.addPrescription(prescription);
    }

    public void addDiagnosis(String diagnosis) {
        this.medicalRecord.addDiagnosis(diagnosis);
    }

    public void addTreatment(String treatment) {
        this.medicalRecord.addTreatment(treatment);
    }
}
