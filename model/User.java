package model;

import java.util.Date;

public class User {
    private String userID;
    private String password;
    private UserRole role;
    private String name;
    private boolean isMale;
    private int age;
    private String email;

    public User(String userId, String password, UserRole role, String name, boolean isMale, int age, String email) {
        // constructor logic
        this.userID = userId;
        this.password = password;
        this.role = role;
        this.name = name;
        this.isMale = isMale;
        this.age = age;
        this.email = email;

    }

    public String getUserId() {
        return userID;
    }

    public UserRole getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public boolean isMale() {
        return isMale;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public boolean login(String password) {
        // login logic
        if (this.password.equals(password)) {
            return true;
        }
        else {
            return false;
        }
    }

    public void changePassword(String newPassword) {
        // change password logic
        this.password = newPassword;
    }

    public void updatePersonalInfo(String email) {
        // update personal info logic
        this.email = email;
    }

    public void logout() {
        // logout logic
        return;
    }
}
