package model;

import java.util.List;

public class MedicalRecord {
    private List<String> prescriptions;
    private List<String> diagnoses;
    private List<String> treatments;

    public MedicalRecord(List<String> prescriptions, List<String> diagnoses, List<String> treatments) {
        // constructor logic
        this.prescriptions = prescriptions;
        this.diagnoses = diagnoses;
        this.treatments = treatments;
    }

    public List<String> getPrescriptions () {
        return prescriptions;
    }

    public List<String> getDiagnoses () {
        return diagnoses;
    }

    public List<String> getTreatments () {
        return treatments;
    }

    public void addPrescription(String prescription) {
        prescriptions.add(prescription);
    }

    public void addDiagnosis(String diagnosis) {
        diagnoses.add(diagnosis);
    }

    public void addTreatment(String treatment) {
        treatments.add(treatment);
    }
}
