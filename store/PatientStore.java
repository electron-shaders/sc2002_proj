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

/**
 * The PatientStore class is responsible for managing the storage and retrieval of Patient records.
 * It supports loading patient data from a CSV file, adding, removing, upserting, and retrieving patient records.
 */
public class PatientStore {
    /**
     * A static variable to keep track of the next unique identifier for patients.
     * It is initialized to 0 and increments each time a new patient is added.
     * It may be updated when loading initial data from a CSV file.
     */
    private static int nextId = 0;

    /**
     * A static HashMap to store patient records.
     * The key is the patient ID and the value is the patient record.
     */
    private static HashMap<String, Patient> patients = new HashMap<String, Patient>();

    /**
     * Loads staff data from a CSV file specified by the given path.
     * The CSV file should specify the following fields in its first row:
     * <ul>
     *  <li>Patient ID</li>
     *  <li>Name</li>
     *  <li>Date of Birth</li>
     *  <li>Gender</li>
     *  <li>Blood Type</li>
     *  <li>Contact Information</li>
     * </ul>
     * The method parses each row and creates a Patient object for each valid record.
     *
     * @param path The path to the CSV file to be loaded
     * @throws FileNotFoundException if the file does not exist
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * Adds a new patient record to the store and generates a user ID for the record.
     * The user ID is prefixed with "P" followed by a 4-digit number.
     *
     * @param record The patient record to be added.
     * @return The unique identifier assigned to the patient
     */
    public static String addRecord(Patient record) {
        String id = "P" + String.format("%04d", ++nextId);
        patients.put(id, record);
        record.setUserId(id);
        return id;
    }

    /**
     * Removes a patient record from the store based on the given ID.
     *
     * @param id The ID of the patient record to be removed
     */
    public static void removeRecord(String id) {
        patients.remove(id);
    }

    /**
     * Upserts the record of a patient with the given ID.
     *
     * @param id The ID of the patient to be upserted
     * @param record The new patient record.
     */
    public static void updateRecord(String id, Patient record) {
        patients.put(id, record);
    }

    /**
     * Retrieves a list of all patient records.
     *
     * @return A list of the patient records
     */
    public static List<Patient> getRecords() {
        return new ArrayList<Patient>(patients.values());
    }

    /**
     * Retrieves a patient record by the provided user ID.
     *
     * @param id The user ID of the patient to be retrieved.
     * @return The patient record associated with the given ID, or null if no such patient exists.
     */
    public static Patient getRecord(String id) {
        return patients.get(id);
    }
}
