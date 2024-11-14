package store;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import model.Doctor;
import model.UserRole;

public class DoctorStore {
    private static int nextId = 0;
    private static HashMap<String, Doctor> doctors = new HashMap<String, Doctor>();

    public static void load(String path) throws FileNotFoundException, IOException {
        try (Reader reader = new FileReader(path)) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("Staff ID", "Name", "Role", "Gender", "Age", "Email", "Specialty", "Rating", "Rating Count")
                .setSkipHeaderRecord(true)
                .build();
            try (CSVParser csvParser = new CSVParser(reader, csvFormat)) {
                for (CSVRecord csvRecord : csvParser) {
                    String userId = csvRecord.get("Staff ID");
                    String name = csvRecord.get("Name");
                    String roleString = csvRecord.get("Role");
                    UserRole role;
                    switch (roleString) {
                        case "Doctor":
                            role = UserRole.DOCTOR;
                            break;
                        default:
                            continue;
                    }
                    String gender = csvRecord.get("Gender");
                    boolean isMale;
                    switch (gender) {
                        case "Male":
                            isMale = true;
                            break;
                        case "Female":
                            isMale = false;
                            break;
                        default:
                            continue;
                    }
                    int age = Integer.parseInt(csvRecord.get("Age"));
                    String email = csvRecord.get("Email");
                    String specialty = csvRecord.get("Specialty");
                    float rating = Float.parseFloat(csvRecord.get("Rating"));
                    int ratingCount = Integer.parseInt(csvRecord.get("Rating Count"));

                    Doctor doctor = new Doctor(userId, "password", role, name, isMale, age, email, specialty, ratingCount, rating);
                    doctors.put(userId, doctor);
                    nextId = Math.max(nextId, Integer.parseInt(userId.substring(1)));
                }
            }
        }
    }

    public static String addRecord(Doctor record) {
        String id = "D" + String.format("%03d", ++nextId);
        doctors.put(id, record);
        record.setUserId(id);
        return id;
    }

    public static void removeRecord(String id) {
        doctors.remove(id);
    }

    public static void updateRecord(String id, Doctor record) {
        doctors.put(id, record);
    }

    public static List<Doctor> getRecords() {
        return new ArrayList<Doctor>(doctors.values());
    }

    public static Doctor getRecord(String id) {
        return doctors.get(id);
    }
}
