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

public class MedicineStore {
    private static int nextId = 0;
    private static HashMap<String, Medicine> medicines = new HashMap<String, Medicine>();

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

    public static String addRecord(Medicine record) {
        String id = "M" + String.format("%04d", ++nextId);
        medicines.put(id, record);
        record.setMedicineId(id);
        return id;
    }

    public static void removeRecord(String id) {
        medicines.remove(id);
    }

    public static void updateRecord(String id, Medicine record) {
        medicines.put(id, record);
    }

    public static List<Medicine> getRecords() {
        return new ArrayList<Medicine>(medicines.values());
    }

    public static Medicine getRecord(String id) {
        return medicines.get(id);
    }
}
