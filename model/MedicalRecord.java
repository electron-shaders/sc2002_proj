package model;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecord {
    private List<String> prescriptions;
    private List<String> diagnoses;
    private List<String> treatments;

    // MedicalRecord is initialized with empty lists
    public MedicalRecord() {
        this.prescriptions = new ArrayList<String>();
        this.diagnoses = new ArrayList<String>();
        this.treatments = new ArrayList<String>();
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
