import store.DoctorStore;
import store.MedicineStore;
import store.PatientStore;
import store.StaffStore;
import view.LoginView;

public class HMS {
    public static void main(String[] args) {
        PatientStore.load("data/Patient_List.csv");
        DoctorStore.load("data/Staff_List.csv");
        StaffStore.load("data/Staff_List.csv");
        MedicineStore.load("data/Medicine_List.csv");

        new LoginView().launch();
    }
}
