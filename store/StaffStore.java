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

import model.User;
import model.UserRole;

/**
 * The StaffStore class is the underlying backstore for staff data (excluding doctors) in the application.
 * It supports loading initial staff data from a CSV file, adding, removing, updating, and retrieving staff records.
 */
public class StaffStore {
    /**
     * A static variable to keep track of the next unique identifier for staff members.
     * It is initialized to 0 and increments each time a new staff member is added.
     * It may be updated when loading initial data from a CSV file.
     */
    private static int nextId = 0;

    /**
     * A static HashMap to store staff records.
     * The key is the staff ID and the value is the staff record.
     */
    private static HashMap<String, User> staff = new HashMap<String, User>();

    /**
     * Loads staff data from a CSV file specified by the given path.
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
     * The method parses each row and creates a User object for each valid record.
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
                        case "Pharmacist":
                            role = UserRole.PHARMACIST;
                            break;
                        case "Administrator":
                            role = UserRole.ADMINISTRATOR;
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

                    User user = new User(userId, "password", role, name, isMale, age, email);
                    staff.put(userId, user);
                    nextId = Math.max(nextId, Integer.parseInt(userId.substring(1)));
                }
            }
        }
    }

    /**
     * Adds a new staff record to the staff store and generates a user ID for the record.
     * The prefix of the user ID is based on the staff's role:
     * <ul>
     *  <li>"P" for Pharmacist</li>
     *  <li>"A" for Administrator</li>
     * </ul>
     * The ID is then suffixed with a 3-digit number.
     *
     * @param record The staff record to be added.
     * @return The user ID assigned to the staff.
     */
    public static String addRecord(User record) {
        String prefix;
        switch (record.getRole()) {
            case PHARMACIST:
                prefix = "P";
                break;
            case ADMINISTRATOR:
                prefix = "A";
                break;
            default:
                return null;
        }
        String id = prefix + String.format("%03d", ++nextId);
        staff.put(id, record);
        record.setUserId(id);
        return id;
    }

    /**
     * Removes a staff record from the store based on the given ID.
     *
     * @param id The ID of the staff record to be removed.
     */
    public static void removeRecord(String id) {
        staff.remove(id);
    }

    /**
     * Updates the record of a staff member with the given ID.
     * 
     * @param id The ID of the staff member to be updated.
     * @param record The new staff record containing updated information.
     *               If the name or email in the new record is null, the corresponding fields will not be updated.
     *               The gender and age fields will always be updated.
     */
    public static void updateRecord(String id, User record) {
        User oldRecord = staff.get(id);
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
    }

    /**
     * Retrieves a list of all staff records.
     *
     * @return A list of the staff records.
     */
    public static List<User> getRecords() {
        return new ArrayList<User>(staff.values());
    }

    /**
     * Retrieves a staff record by the provided user ID.
     *
     * @param id The user ID of the staff to be retrieved.
     * @return The staff record associated with the given ID, or null if no such staff exists.
     */
    public static User getRecord(String id) {
        return staff.get(id);
    }
}
