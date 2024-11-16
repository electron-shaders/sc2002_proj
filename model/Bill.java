package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The Bill class represents a bill for a patient's appointment outcome record.
 * It supports calculating total amount and due amount for the appointment outcome record and generating a (terminal) printable bill.
 */
public class Bill {
    /**
     * The appointment outcome record associated with the bill.
     */
    private AppointmentOutcomeRecord outcomeRecord;

    /**
     * Constructs a new Bill with the specified appointment outcome record.
     *
     * @param outcomeRecord the appointment outcome record associated with the bill
     */
    public Bill(AppointmentOutcomeRecord outcomeRecord) {
        this.outcomeRecord = outcomeRecord;
    }

    /**
     * Getter for the date of the appointment outcome record.
     * @return the date of the appointment outcome record
     */
    public Date getDate() {
        return outcomeRecord.getDate();
    }

    /**
     * Getter for the service type of the appointment outcome record.
     * @return the service type of the appointment outcome record
     */
    public String getServiceType() {
        return outcomeRecord.getServiceType();
    }

    /**
     * Getter for the list of prescriptions in the appointment outcome record.
     * @return the list of prescriptions in the appointment outcome record
     */
    public List<Prescription> getPrescriptions() {
        return outcomeRecord.getPrescriptions();
    }

    /**
     * Calculates the subtotal amount of the bill.
     * @return the subtotal amount of the bill
     */
    public float getSubtotal() {
        float subtotal = 0;
        for (Prescription prescription : outcomeRecord.getPrescriptions()) {
            subtotal += prescription.getMedicine().getPrice();
        }
        return subtotal;
    }

    /**
     * Calculates the due amount of the bill.
     * The dispensed medications are excluded from the due amount.
     * @return the due amount of the bill
     */
    public float getDue() {
        float due = 0;
        for (Prescription prescription : outcomeRecord.getPrescriptions()) {
            if (prescription.getStatus() == PrescriptionStatus.PENDING) {
                due += prescription.getMedicine().getPrice();
            }
        }
        return due;
    }

    /**
     * Generates a printable bill associated with the appointment outcome record.
     * @return a printable String for the bill
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sb.append("======================= Bill =======================\n");
        sb.append("Date: ").append(sdf.format(getDate())).append("\n");
        sb.append("Service Type: ").append(getServiceType()).append("\n");
        sb.append("Prescriptions:\n");
        List<Prescription> prescriptions = getPrescriptions();
        if (prescriptions.isEmpty()) {
            sb.append("    (Empty)\n");
        }
        for (Prescription prescription : prescriptions) {
            sb.append("  - ").append(prescription.getMedicine().getName()).append("\t$").append(prescription.getMedicine().getPrice()).append("\n");
        }
        sb.append("Subtotal: $").append(String.format("%.2f", getSubtotal())).append("\n");
        sb.append("Due: $").append(String.format("%.2f", getDue())).append("\n");
        sb.append("====================================================\n");
        sb.append("Notes: Dispensed medications are excluded from the due amount.\n");
        sb.append("====================================================\n");
        return sb.toString();
    }
}
