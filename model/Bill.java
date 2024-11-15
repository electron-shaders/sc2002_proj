package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Bill {
    private AppointmentOutcomeRecord outcomeRecord;

    public Bill(AppointmentOutcomeRecord outcomeRecord) {
        this.outcomeRecord = outcomeRecord;
    }

    public Date getDate() {
        return outcomeRecord.getDate();
    }

    public String getServiceType() {
        return outcomeRecord.getServiceType();
    }

    public List<Prescription> getPrescriptions() {
        return outcomeRecord.getPrescriptions();
    }

    public float getSubtotal() {
        float subtotal = 0;
        for (Prescription prescription : outcomeRecord.getPrescriptions()) {
            subtotal += prescription.getMedicine().getPrice();
        }
        return subtotal;
    }

    public float getDue() {
        float due = 0;
        for (Prescription prescription : outcomeRecord.getPrescriptions()) {
            if (prescription.getStatus() == PrescriptionStatus.PENDING) {
                due += prescription.getMedicine().getPrice();
            }
        }
        return due;
    }

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
