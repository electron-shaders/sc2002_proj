package model;

/**
 * The Medicine class represents a medicine with details such as stock level, 
 * low stock level alert line, replenishment request status, and price.
 */
public class Medicine {
    /**
     * The unique identifier for the medicine.
     */
    private String medicineId;

    /**
     * The name of the medicine.
     */
    private String name;

    /**
     * The stock level of the medicine.
     */
    private int stock;

    /**
     * The low stock level alert line of the medicine.
     */
    private int lowStockThreshold;

    /**
     * The replenishment request status of the medicine.
     */
    private boolean isRequestingReplenishment;

    /**
     * The price of the medicine.
     */
    private float price;

    /**
     * Constructs a new Medicine with the specified details.
     *
     * @param medicineId                the unique identifier for the medicine
     * @param name                      the name of the medicine
     * @param stock                     the stock level of the medicine
     * @param lowStockThreshold         the low stock level alert line of the medicine
     * @param isRequestingReplenishment the replenishment request status of the medicine
     * @param price                     the price of the medicine
     */
    public Medicine(String medicineId, String name, int stock, int lowStockThreshold, boolean isRequestingReplenishment, float price) {
        this.medicineId = medicineId;
        this.name = name;
        this.stock = stock;
        this.lowStockThreshold = lowStockThreshold;
        this.isRequestingReplenishment = isRequestingReplenishment;
        this.price = price;
    }

    /**
     * Getter for the medicine ID of the medicine.
     * @return the medicine ID of the medicine
     */
    public String getMedicineId() {
        return medicineId;
    }

    /**
     * Setter for the medicine ID of the medicine.
     * @param medicineId the new medicine ID of the medicine
     */
    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    /**
     * Getter for the name of the medicine.
     * @return the name of the medicine
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the stock level of the medicine.
     * @return the stock level of the medicine
     */
    public int getStock() {
        return stock;
    }

    /**
     * Setter for the stock level of the medicine.
     * @param stock the new stock level of the medicine
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Getter for the low stock level alert line of the medicine.
     * @return the low stock level alert line of the medicine
     */
    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    /**
     * Setter for the low stock level alert line of the medicine.
     * @param lowStockThreshold the new low stock level alert line of the medicine
     */
    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }
    
    /**
     * Getter for the replenishment request status of the medicine.
     * @return the replenishment request status of the medicine, true if requesting replenishment, false otherwise
     */
    public boolean getIsRequestingReplenishment() {
        return isRequestingReplenishment;
    }

    /**
     * Setter for the replenishment request status of the medicine.
     * @param isRequestingReplenishment the new replenishment request status of the medicine, true if requesting replenishment, false otherwise
     */
    public void setIsRequestingReplenishment(boolean isRequestingReplenishment) {
        this.isRequestingReplenishment = isRequestingReplenishment;
    }

    /**
     * Getter for the price of the medicine.
     * @return the price of the medicine
     */
    public float getPrice() {
        return price;
    }

    /**
     * Setter for the price of the medicine.
     * @param price the new price of the medicine
     */
    public void setPrice(float price) {
        this.price = price;
    }
}
