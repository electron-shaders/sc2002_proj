package view;

import java.util.HashMap;

import model.User;
import model.Doctor;
import model.Patient;

/**
 * Factory class for creating and managing dashboard views for different users upon successful login.
 * This class maintains a mapping between users and their associated dashboard views.
 */
public class DashboardViewFactory {
    /**
     * Caches the mapping between a User and their associated dashboard views.
     */
    private static HashMap<User, DashboardView> dashboardViews = new HashMap<User, DashboardView>();

    /**
     * Returns the dashboard view for the given user based on their role.
     *
     * @param user the user for whom the dashboard view is to be retrieved
     * @return the dashboard view associated with the user
     */
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
