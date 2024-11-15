import store.DoctorStore;
import store.MedicineStore;
import store.PatientStore;
import store.StaffStore;
import view.LoginView;

public class HMS {
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
