package view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import model.User;
import observer.ISubscriber;

/**
 * The DashboardView class provides common methods for dashboard views, including
 * getting validated user choices, displaying notifications, tips, success and error messages,
 * and a form for changing user password.
 */
public abstract class DashboardView implements IView, ISubscriber{
    /**
     * Prompts the user to enter a choice within a specified range and returns the validated choice.
     * Repeatedly prompts the user until a valid choice is entered.
     *
     * @param min The minimum valid choice (inclusive).
     * @param max The maximum valid choice (inclusive).
     * @return The user's validated choice as an integer.
     */
    public int getChoice(int min, int max) {
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
                sc.nextLine();
                showError("Invalid choice. Please enter a number between " + min + " and " + max + ".");
                System.out.println();
            }
        }
        return choice;
    }

    /**
     * Displays a list of notifications.
     *
     * @param notifications A list of notification messages to be displayed
     */
    public void showNotifications(List<String> notifications){
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println("NOTIFICATIONS: ");
        int count = 1;
        for(String notification : notifications){
            System.out.println("(" + count + ") " + notification);
            count++;
        }
        System.out.println("------------------------------------------------------------------------------------------------------");
    }

    /**
     * Displays a list of tips.
     *
     * @param tips A list of strings containing the tips to be displayed.
     */
    public void showUserTips(List<String> tips){
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println("TIPS: ");
        int count = 1;
        for(String tip : tips){
            System.out.println("(" + count + ") " + tip);
            count++;
        }
        System.out.println("------------------------------------------------------------------------------------------------------");
    }

    /**
     * Displays a success message upon a successful action.
     *
     * @param message The success message to be displayed.
     */
    public void showSuccess(String message){
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println("[SUCCESS]: " + message);
        System.out.println("------------------------------------------------------------------------------------------------------");
    }

    /**
     * Displays a success empty message upon a successful action.
     */
    public void showSuccess(){
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println("[SUCCESS]");
        System.out.println("------------------------------------------------------------------------------------------------------");
    }

    /**
     * Displays an error message upon an unsuccessful action.
     *
     * @param message The error message to be displayed.
     */
    public void showError(String message){
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println("[ERROR]: " + message);
        System.out.println("------------------------------------------------------------------------------------------------------");
    }

    /**
     * Displays a form to change the user's password.
     * 
     * @param user The user whose password is to be changed.
     */
    public void showChangePasswordForm(User user){
        Scanner sc = new Scanner(System.in);
        String newPassword, check;
        while(true){
            System.out.print("Please enter your new password: ");
            newPassword = sc.nextLine();
            System.out.print("Please enter your new password again to confirm: ");
            check = sc.nextLine();

            if(newPassword.equals(check)){
                break;
            }
            else{
                showError("Passwords do not match. Please try again.");
            }
        }

        try{
            user.changePassword(newPassword);
            showSuccess("Password changed successfully");
        }catch (Exception e){
            showError(e.getMessage());
        }
    }
}