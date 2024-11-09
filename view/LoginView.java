package view;

import java.util.InputMismatchException;
import java.util.Scanner;

import model.UserRole;
import store.PatientStore;
import store.DoctorStore;
import store.StaffStore;

public class LoginView implements IView{
    private boolean loginSuccess;
    User user;
    UserRole userRole;

    public LoginView(){
        loginSuccess = false;
        user = null;
        userRole = null;
    }

    private int getChoice(int min, int max) {
        int choice = 0;
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Please enter your choice (" + min + " - " + max + "): ");
                choice = sc.nextInt();
                if (choice >= min && choice <= max) {
                    break;
                }
                showError("Invalid choice. Please enter a number between " + min + " and " + max + ".");
                System.out.println();
            } catch (InputMismatchException e) {
                showError("Invalid choice. Please enter a number between " + min + " and " + max + ".");
                System.out.println();
            }
        }
        return choice;
    }

    public void launch(){
        Scanner sc = new Scanner(System.in);
        System.out.println("======================================================================================================");
        System.out.println("|                                     HOSPITAL MANAGEMENT SYSTEM                                     |");
        System.out.println("|                                                LOGIN                                               |");
        System.out.println("======================================================================================================");

        while(!loginSuccess){
            //Get input for userRole
            int numOfRoles = UserRole.values().length;
            System.out.println("User roles:");
            int count = 1;
            for(UserRole role : UserRole.values()){
                System.out.println(count + ". " + role);
                count++;
            }
            int choice = getChoice(1, numOfRoles);
            userRole = UserRole.values()[choice-1];
            
            //Get input for userID and password
            System.out.print  ("Enter user ID: ");
            String userId = sc.nextLine();
            System.out.print  ("Enter password: ");
            String password = sc.nextLine();

            loginSuccess = authenticate(userRole, userId, password);
        }
        sc.close();
        navigateToDashboard(user);
    }

    public boolean authenticate(UserRole userRole, String userId, String password){
        switch(userRole){
            case PATIENT:
                user = PatientStore.getRecord(userId);
                break;
            case DOCTOR:
                user = DoctorStore.getRecord(userId);
                break;
            case PHARMACIST:
            case ADMINISTRATOR:
                user = StaffStore.getRecord(userId);
                break;
            default:
                showError("Unable to find role specific Store class. Update to authenticate() in LoginView.java may be required.");
                return false;
        }

        //Check userID
        if(user == null){
            showError("User ID not found.");
            return false;
        }
        
        //Check password
        if(!user.login(password)){
            showError("Incorrect password.");
            return false;
        }

        return true;
    }

    public void showError(String message){
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println("[ERROR]: " + message);
        System.out.println("------------------------------------------------------------------------------------------------------");
    }

    public void navigateToDashboard(User user){
        userRole = user.getRole();
        switch(userRole){
            case UserRole.PATIENT:
                PatientView patientView = new PatientView(user);
                patientView.launch();
                break;
            case DOCTOR:
                DoctorView doctorView = new DoctorView(user);
                doctorView.launch();
                break;
            case PHARMACIST:
                PharmacistView pharmacistView = new PharmacistView(user);
                pharmacistView.launch();
                break;
            case ADMINISTRATOR:
                AdministratorView administratorView = new AdministratorView(user);
                administratorView.launch();
                break;
            default:
                showError("Unable to find role specific View class. Update to navigateToDashboard() in LoginView.java may be required.");
                loginSuccess = false;
                launch();
        }
    }
}