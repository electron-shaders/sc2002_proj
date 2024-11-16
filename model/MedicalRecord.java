package model;

import java.util.ArrayList;
import java.util.List;

/**
 * The MedicalRecord class represents a medical record, which is part of a Patient.
 * It contains information about the patient's past prescriptions, diagnoses, and treatments.
 */
public class MedicalRecord {
    /**
     * The list of past prescriptions in the medical record.
     */
    private List<String> prescriptions;

    /**
     * The list of past diagnoses in the medical record.
     */
    private List<String> diagnoses;

    /**
     * The list of past treatments in the medical record.
     */
    private List<String> treatments;

    /**
     * Constructs a new MedicalRecord with empty lists of prescriptions, diagnoses, and treatments.
     */
    public MedicalRecord() {
        this.prescriptions = new ArrayList<String>();
        this.diagnoses = new ArrayList<String>();
        this.treatments = new ArrayList<String>();
    }

    /**
     * Getter for the list of past prescriptions in the medical record.
     * @return the list of past prescriptions in the medical record
     */
    public List<String> getPrescriptions () {
        return prescriptions;
    }

    /**
     * Getter for the list of past diagnoses in the medical record.
     * @return the list of past diagnoses in the medical record
     */
    public List<String> getDiagnoses () {
        return diagnoses;
    }

    /**
     * Getter for the list of past treatments in the medical record.
     * @return the list of past treatments in the medical record
     */
    public List<String> getTreatments () {
        return treatments;
    }

    /**
     * Adds a prescription to the list of past prescriptions in the medical record.
     * @param prescription the prescription to add
     */
    public void addPrescription(String prescription) {
        prescriptions.add(prescription);
    }

    /**
     * Adds a diagnosis to the list of past diagnoses in the medical record.
     * @param diagnosis the diagnosis to add
     */
    public void addDiagnosis(String diagnosis) {
        diagnoses.add(diagnosis);
    }

    /**
     * Adds a treatment plan to the list of past treatments in the medical record.
     * @param treatment the treatment to add
     */
    public void addTreatment(String treatment) {
        treatments.add(treatment);
    }
}
