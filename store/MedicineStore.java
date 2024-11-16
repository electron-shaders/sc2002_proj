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

import model.Medicine;

/**
 * The MedicineStore class is responsible for managing the storage and retrieval of Medicine records.
 * It supports loading medicine data from a CSV file, adding, removing, upserting, and retrieving medicine records.
 */
public class MedicineStore {
    /**
     * A static variable to keep track of the next unique identifier for medicines.
     * It is initialized to 0 and increments each time a new medicine is added.
     * It may be updated when loading initial data from a CSV file.
     */
    private static int nextId = 0;

    /**
     * A static HashMap to store medicine records.
     * The key is the medicine ID and the value is the medicine record.
     */
    private static HashMap<String, Medicine> medicines = new HashMap<String, Medicine>();

    /**
     * Loads medicine data from a CSV file specified by the given path.
     * The CSV file should specify the following fields in its first row:
     * <ul>
     *  <li>Medicine Name</li>
     *  <li>Initial Stock</li>
     *  <li>Low Stock Level Alert</li>
     *  <li>Price</li>
     * </ul>
     * The method parses each row and creates a Medicine object for each valid record.
     *
     * @param path The path to the CSV file to be loaded
     * @throws FileNotFoundException if the file does not exist
     * @throws IOException if an I/O error occurs
     */
    public static void load(String path) throws FileNotFoundException, IOException {
        try (Reader reader = new FileReader(path)) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("Medicine Name", "Initial Stock", "Low Stock Level Alert", "Price")
                .setSkipHeaderRecord(true)
                .build();
            try (CSVParser csvParser = new CSVParser(reader, csvFormat)) {
                for (CSVRecord csvRecord : csvParser) {
                    String medicineName = csvRecord.get("Medicine Name");
                    int initialStock = Integer.parseInt(csvRecord.get("Initial Stock"));
                    int lowStockLevelAlert = Integer.parseInt(csvRecord.get("Low Stock Level Alert"));
                    float price = Float.parseFloat(csvRecord.get("Price"));
                    String id = "M" + String.format("%04d", ++nextId);

                    Medicine medicine = new Medicine(id, medicineName, initialStock, lowStockLevelAlert, false, price);
                    medicines.put(id, medicine);
                    nextId = Math.max(nextId, Integer.parseInt(id.substring(1)));
                }
            }
        }
    }

    /**
     * Adds a new medicine record to the store and generates a medicine ID for the record.
     * The medicine ID is prefixed with "M" followed by a 4-digit number.
     *
     * @param record The medicine record to be added
     * @return The unique identifier assigned to the medicine
     */
    public static String addRecord(Medicine record) {
        String id = "M" + String.format("%04d", ++nextId);
        medicines.put(id, record);
        record.setMedicineId(id);
        return id;
    }

    /**
     * Removes a medicine from the store based on the given ID.
     *
     * @param id The ID of the medicine record to be removed
     */
    public static void removeRecord(String id) {
        medicines.remove(id);
    }

    /**
     * Upserts the record of a medicine with the given ID.
     *
     * @param id The ID of the medicine to be upserted
     * @param record The new medicine record
     */
    public static void updateRecord(String id, Medicine record) {
        medicines.put(id, record);
    }

    /**
     * Retrieves a list of all medicine records .
     *
     * @return A list of the medicine records
     */
    public static List<Medicine> getRecords() {
        return new ArrayList<Medicine>(medicines.values());
    }

    /**
     * Retrieves a medicine record by the provided medicine ID.
     *
     * @param id The medicine ID of the medicine to be retrieved
     * @return The medicine record associated with the given ID, or null if no such medicine exists
     */
    public static Medicine getRecord(String id) {
        return medicines.get(id);
    }
}
