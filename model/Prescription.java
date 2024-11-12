package model;

public class Prescription {
    private Medicine medicine;
    private PrescriptionStatus status;

    public Prescription(Medicine medicine, PrescriptionStatus status) {
        this.medicine = medicine;
        this.status = status;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public PrescriptionStatus getStatus() {
        return status;
    }

    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }
    
}
