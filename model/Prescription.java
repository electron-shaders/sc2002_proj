package model;

/**
 * The Prescription class represents a prescription item provided by a doctor.
 * It contains information about the medicine and the status of the prescription.
 */
public class Prescription {
    /**
     * The medicine prescribed in the prescription item.
     */
    private Medicine medicine;

    /**
     * The status of the prescription item.
     */
    private PrescriptionStatus status;

    /**
     * Constructs a new Prescription with the specified medicine and status.
     *
     * @param medicine the medicine prescribed in the prescription item
     * @param status   the status of the prescription item
     */
    public Prescription(Medicine medicine, PrescriptionStatus status) {
        this.medicine = medicine;
        this.status = status;
    }

    /**
     * Getter for the medicine prescribed in the prescription item.
     * @return the medicine prescribed in the prescription item
     */
    public Medicine getMedicine() {
        return medicine;
    }

    /**
     * Getter for the status of the prescription item.
     * @return the status of the prescription item
     */
    public PrescriptionStatus getStatus() {
        return status;
    }

    /**
     * Setter for the status of the prescription item.
     * @param status the new status of the prescription item
     */
    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }
}
