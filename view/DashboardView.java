package view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import model.User;
import observer.ISubscriber;

public abstract class DashboardView implements IView, ISubscriber{
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
                showError("Invalid choice. Please enter a number between " + min + " and " + max + ".");
                System.out.println();
            }
        }
        return choice;
    }

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

    public void showSuccess(String message){
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println("[SUCCESS]: " + message);
        System.out.println("------------------------------------------------------------------------------------------------------");
    }

    public void showSuccess(){
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println("[SUCCESS]");
        System.out.println("------------------------------------------------------------------------------------------------------");
    }

    public void showError(String message){
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println("[ERROR]: " + message);
        System.out.println("------------------------------------------------------------------------------------------------------");
    }

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