package model;

/**
 * The IUser interface provides the contract for user-related operations.
 * Implementing user classes should provide implementations for user login,
 * password change, personal information update, and logout functionalities.
 */
public interface IUser {
    /**
     * Logs in the user with the specified password.
     * @param password the password to try logging in with
     * @return true if the user is successfully logged in, false otherwise
     */
    public boolean login(String password);

    /**
     * Changes the password of the user to the new password.
     * @param newPassword the new password
     */
    public void changePassword(String newPassword);

    /**
     * Updates the personal information (email) of the user with the new email.
     * @param email the new email
     */
    public void updatePersonalInfo(String email);

    /**
     * Logs out the user.
     */
    public void logout();
}