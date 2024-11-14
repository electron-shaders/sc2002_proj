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

public class StaffStore {
    private static int nextId = 0;
    private static HashMap<String, User> staff = new HashMap<String, User>();

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
                prefix = "S";
                break;
        }
        String id = prefix + String.format("%03d", ++nextId);
        staff.put(id, record);
        record.setUserId(id);
        return id;
    }

    public static void removeRecord(String id) {
        staff.remove(id);
    }

    public static void updateRecord(String id, User record) {
        staff.put(id, record);
    }

    public static List<User> getRecords() {
        return new ArrayList<User>(staff.values());
    }

    public static User getRecord(String id) {
        return staff.get(id);
    }
}
