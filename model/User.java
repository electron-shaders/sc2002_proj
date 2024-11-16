package model;

/**
 * The User class represents a user in the system.
 * It implements the IUser interface and contains user-related information and methods.
 */
public class User implements IUser {
    /**
     * The unique identifier for the user.
     */
    private String userID;

    /**
     * The password for the user.
     */
    private String password;

    /**
     * The role of the user.
     */
    private UserRole role;

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The gender of the user:
     * true for Male, false for Female.
     */
    private boolean isMale;

    /**
     * The age of the user.
     */
    private int age;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * Constructs a new User with the specified details.
     *
     * @param userId   the user ID for the user
     * @param password the password for the user
     * @param role     the role of the user (e.g., ADMIN, USER)
     * @param name     the name of the user
     * @param isMale   the gender of the user, true for Male, false for Female
     * @param age      the age of the user
     * @param email    the email address of the user
     */
    public User(String userId, String password, UserRole role, String name, boolean isMale, int age, String email) {
        this.userID = userId;
        this.password = password;
        this.role = role;
        this.name = name;
        this.isMale = isMale;
        this.age = age;
        this.email = email;
    }

    /**
     * Getter for the user ID of the user.
     * @return the user ID of the user
     */
    public String getUserId() {
        return userID;
    }

    /**
     * Setter for the user ID of the user.
     * @param userId the new user ID for the user
     */
    public void setUserId(String userId) {
        this.userID = userId;
    }

    /**
     * Getter for the role of the user.
     * @return the role of the user
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Getter for the name of the user.
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name of the user.
     * @param name the new name for the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the gender of the user.
     * @return the gender of the user, true for Male, false for Female
     */
    public boolean getIsMale() {
        return isMale;
    }

    /**
     * Setter for the gender of the user.
     * @param isMale the new gender of the user, true for Male, false for Female
     */
    public void setIsMale(boolean isMale) {
        this.isMale = isMale;
    }

    /**
     * Getter for the age of the user.
     * @return the age of the user
     */
    public int getAge() {
        return age;
    }

    /**
     * Setter for the age of the user.
     * @param age the new age for the user
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Getter for the email address of the user.
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Try to login with the given password.
     * @param password the password to try
     * @return true if the password is correct, false otherwise
     */
    public boolean login(String password) {
        return this.password.equals(password);
    }

    /**
     * Change the password of the user.
     * @param newPassword the new password for the user
     */
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Update the personal information (email) of the user.
     * @param email the new email address for the user
     */
    public void updatePersonalInfo(String email) {
        this.email = email;
    }

    /**
     * Log out the user.
     */
    public void logout() {}
}
