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

/**
 * The DoctorStore class is responsible for managing the storage and retrieval of Doctor records.
 * It supports loading doctor data from a CSV file, adding, removing, updating, and retrieving doctor records.
 */
public class DoctorStore {
    /**
     * A static variable to keep track of the next unique identifier for doctors.
     * It is initialized to 0 and increments each time a new patient is added.
     * It may be updated when loading initial data from a CSV file.
     */
    private static int nextId = 0;

    /**
     * A static HashMap to store doctor records.
     * The key is the doctor ID and the value is the doctor record.
     */
    private static HashMap<String, Doctor> doctors = new HashMap<String, Doctor>();

    /**
     * Loads doctor data from a CSV file specified by the given path.
     * The CSV file should specify the following fields in its first row:
     * <ul>
     *  <li>Staff ID</li>
     *  <li>Name</li>
     *  <li>Role</li>
     *  <li>Gender</li>
     *  <li>Age</li>
     *  <li>Email</li>
     *  <li>Specialty</li>
     *  <li>Rating</li>
     *  <li>Rating Count</li>
     * </ul>
     * The method parses each row and creates a Doctor object for each valid record.
     *
     * @param path The path to the CSV file to be loaded
     * @throws FileNotFoundException if the file does not exist
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * Adds a new doctor record to the store and generates a doctor ID for the record.
     * The doctor ID is prefixed with "D" followed by a 3-digit number.
     *
     * @param record The doctor record to be added
     * @return The unique identifier assigned to the doctor
     */
    public static String addRecord(Doctor record) {
        String id = "D" + String.format("%03d", ++nextId);
        doctors.put(id, record);
        record.setUserId(id);
        return id;
    }

    /**
     * Removes a doctor from the store based on the given ID.
     *
     * @param id The ID of the doctor record to be removed
     */
    public static void removeRecord(String id) {
        doctors.remove(id);
    }

    /**
     * Updates the record of a doctor with the given ID.
     *
     * @param id The ID of the doctor to be upserted
     * @param record The new staff record containing updated information.
     *               If the name, email or specialty in the new record is null, the corresponding fields will not be updated.
     *               The gender and age fields will always be updated.
     */
    public static void updateRecord(String id, Doctor record) {
        Doctor oldRecord = doctors.get(id);
        if (oldRecord == null || record == null) {
            return;
        }
        if (record.getName() != null) {
            oldRecord.setName(record.getName());
        }
        oldRecord.setIsMale(record.getIsMale());
        oldRecord.setAge(record.getAge());
        if (record.getEmail() != null) {
            oldRecord.updatePersonalInfo(record.getEmail());
        }
        if (record.getSpecialty() != null) {
            oldRecord.setSpecialty(record.getSpecialty());
        }
    }

    /**
     * Retrieves a list of all doctor records.
     *
     * @return A list of the doctor records
     */
    public static List<Doctor> getRecords() {
        return new ArrayList<Doctor>(doctors.values());
    }

    /**
     * Retrieves a doctor record by the provided doctor ID.
     *
     * @param id The doctor ID of the doctor to be retrieved
     * @return The doctor record associated with the given ID, or null if no such doctor exists
     */
    public static Doctor getRecord(String id) {
        return doctors.get(id);
    }
}
