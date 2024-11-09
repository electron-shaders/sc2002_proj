package model;

public interface IUser {
    public boolean login();
    public void changePassword(String newPassword);
    public void updatePersonalInfo(String email, String contactNumber);
    public void logout();
}