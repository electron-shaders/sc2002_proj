import store.DoctorStore;
import store.MedicineStore;
import store.PatientStore;
import store.StaffStore;
import view.LoginView;

/**
 * The HMS class serves as the main system class for
 * the Hospital Management System (HMS) application
 * where the main method resides.
 */
public class HMS {
    /**
     * <p>The main method performs the following system initialization tasks:</p>
     * <ul>
     *   <li>Loads patient data from "data/Patient_List.csv".</li>
     *   <li>Loads doctor data from "data/Staff_List.csv".</li>
     *   <li>Loads staff data from "data/Staff_List.csv".</li>
     *   <li>Loads medicine data from "data/Medicine_List.csv".</li>
     *   <li>Launches the login view.</li>
     * </ul>
     * 
     * <p>If an error occurs during data loading, the application will print the stack trace and exit with status code 1.</p>
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            PatientStore.load("data/Patient_List.csv");
            DoctorStore.load("data/Staff_List.csv");
            StaffStore.load("data/Staff_List.csv");
            MedicineStore.load("data/Medicine_List.csv");
        } catch (Exception e) {
            System.out.println("Error loading data from csv files");
            e.printStackTrace();
            System.exit(1);
        }

        new LoginView().launch();
    }
}
