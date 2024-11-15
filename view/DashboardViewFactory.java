package view;

import java.util.HashMap;

import model.User;
import model.Doctor;
import model.Patient;

public class DashboardViewFactory {
    private static HashMap<User, DashboardView> dashboardViews = new HashMap<User, DashboardView>();

    public static DashboardView getDashboardView(User user) {
        if (dashboardViews.containsKey(user)) {
            return dashboardViews.get(user);
        }

        DashboardView dashboardView = null;
        switch (user.getRole()) {
            case PATIENT:
                dashboardView = new PatientView((Patient) user);
                break;
            case DOCTOR:
                dashboardView = new DoctorView((Doctor) user);
                break;
            case PHARMACIST:
                dashboardView = new PharmacistView(user);
                break;
            case ADMINISTRATOR:
                dashboardView = new AdministratorView(user);
                break;
        }

        dashboardViews.put(user, dashboardView);
        return dashboardView;
    }
}
