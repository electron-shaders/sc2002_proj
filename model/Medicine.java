package model;

public class Medicine {
    private String medicineId;
    private String name;
    private int stock;
    private int lowStockThreshold;
    private boolean isRequestingReplenishment;
    private float price;

    public Medicine(String medicineId, String name, int stock, int lowStockThreshold, boolean isRequestingReplenishment, float price) {
        this.medicineId = medicineId;
        this.name = name;
        this.stock = stock;
        this.lowStockThreshold = lowStockThreshold;
        this.isRequestingReplenishment = isRequestingReplenishment;
        this.price = price;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public String setMedicineId(String medicineId) {
        return this.medicineId = medicineId;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }
    

    public boolean getIsRequestingReplenishment() {
        return isRequestingReplenishment;
    }

    public void setIsRequestingReplenishment(boolean isRequestingReplenishment) {
        this.isRequestingReplenishment = isRequestingReplenishment;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

}
