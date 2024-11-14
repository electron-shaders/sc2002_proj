package model;

public interface IUser {
    public boolean login();
    public void changePassword(String newPassword);
    public void updatePersonalInfo(String email);
    public void logout();
}