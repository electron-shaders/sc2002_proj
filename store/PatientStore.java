package store;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import model.MedicalRecord;
import model.Patient;
import model.UserRole;

public class PatientStore {
    private static int nextId = 0;
    private static HashMap<String, Patient> patients = new HashMap<String, Patient>();

    public static void load(String path) throws FileNotFoundException, IOException {
        try (Reader reader = new FileReader(path)) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("Patient ID", "Name", "Date of Birth", "Gender", "Blood Type", "Contact Information")
                .setSkipHeaderRecord(true)
                .build();
            try (CSVParser csvParser = new CSVParser(reader, csvFormat)) {
                for (CSVRecord csvRecord : csvParser) {
                    String userId = csvRecord.get("Patient ID");
                    String name = csvRecord.get("Name");
                    String dateOfBirthString = csvRecord.get("Date of Birth");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateOfBirth;
                    try {
                        dateOfBirth = sdf.parse(dateOfBirthString);
                    } catch (ParseException e) {
                        continue;
                    }
                    String genderString = csvRecord.get("Gender");
                    boolean isMale;
                    switch (genderString) {
                        case "Male":
                            isMale = true;
                            break;
                        case "Female":
                            isMale = false;
                            break;
                        default:
                            continue;
                    }
                    String bloodType = csvRecord.get("Blood Type");
                    String email = csvRecord.get("Contact Information");

                    Patient patient = new Patient(userId, "password", UserRole.PATIENT, name, isMale, email, dateOfBirth, bloodType, new MedicalRecord());
                    patients.put(userId, patient);
                    nextId = Math.max(nextId, Integer.parseInt(userId.substring(1)));
                }
            }
        }
    }

    public static String addRecord(Patient record) {
        String id = "P" + String.format("%04d", ++nextId);
        patients.put(id, record);
        record.setUserId(id);
        return id;
    }

    public static void removeRecord(String id) {
        patients.remove(id);
    }

    public static void updateRecord(String id, Patient record) {
        patients.put(id, record);
    }

    public static List<Patient> getRecords() {
        return new ArrayList<Patient>(patients.values());
    }

    public static Patient getRecord(String id) {
        return patients.get(id);
    }
}
