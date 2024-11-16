package model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The Doctor class represents a doctor in the system.
 * It is a User and contains doctor-specific information like the specialty and availability.
 * It implements the Comparable interface to support sorting doctors by their ratings.
 */
public class Doctor extends User implements Comparable<Doctor> {
    /**
     * The specialty of the doctor.
     */
    private String specialty;

    /**
     * The number of ratings received by the doctor.
     */
    private int ratingCount;

    /**
     * The average rating of the doctor.
     */
    private float rating;

    /**
     * The availability of the doctor for appointments.
     */
    private Map<Date, Boolean> availability;

    /**
     * Constructs a new Doctor with the specified details.
     *
     * @param userId      the user ID for the doctor
     * @param password    the password for the doctor
     * @param role        the role of the doctor (DOCTOR)
     * @param name        the name of the doctor
     * @param isMale      the gender of the user, true for Male, false for Female
     * @param age         the age of the user
     * @param email       the email address of the user
     * @param specialty   the specialty of the doctor
     * @param ratingCount the number of ratings received by the doctor
     * @param rating      the average rating of the doctor
     * @see User
     */
    public Doctor(String userId, String password, UserRole role, String name, boolean isMale, int age, String email, String specialty, int ratingCount, float rating) {
        super(userId, password, role, name, isMale, age, email);
        this.specialty = specialty;
        this.ratingCount = ratingCount;
        this.rating = rating;
        this.availability = new HashMap<Date, Boolean>();
    }

    /**
     * Compares this doctor with another doctor based on their ratings.
     * @param o the doctor to be compared
     * @return -1, 0, or 1 if this doctor is rated higher, equal, or lower than the other doctor, respectively
     */
    public int compareTo(Doctor o) {
        float diff = this.rating - o.rating;
        if (diff > 0)
            return -1;
        else if (diff < 0)
            return 1;
        else
            return 0;
    }

    /**
     * Getter for the specialty of the doctor.
     * @return the specialty of the doctor
     */
    public String getSpecialty() {
        return specialty;
    }

    /**
     * Setter for the specialty of the doctor.
     * @param specialty the new specialty of the doctor
     */
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    /**
     * Getter for the number of ratings received by the doctor.
     * @return the number of ratings received by the doctor
     */
    public int getRatingCount() {
        return ratingCount;
    }

    /**
     * Increments the number of ratings received by the doctor by 1.
     */
    public void incrementRatingCount() {
        ratingCount++;
    }

    /**
     * Getter for the average rating of the doctor.
     * @return the average rating of the doctor
     */
    public float getRating() {
        return rating;
    }

    /**
     * Setter for the average rating of the doctor.
     * @param rating the new average rating of the doctor
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * Getter for the availability of the doctor for appointments.
     * @return the availability of the doctor for appointments
     */
    public Map<Date, Boolean> getAvailability() {
        return availability;
    }

    /**
     * Adds the specified date to the availability of the doctor.
     * @param date the date to be added to the availability
     */
    public void addAvailability(Date date) {
        availability.put(date, true);
    }

    /**
     * Removes the specified date from the availability of the doctor.
     * @param date the date to be removed from the availability
     */
    public void removeAvailability(Date date) {
        availability.put(date, false);
    }

    /**
     * Checks if the doctor is available on the specified date.
     * @param date the date to be checked for availability
     * @return true if the doctor is available on the date, false otherwise
     */
    public boolean isAvailable(Date date) {
        return availability.getOrDefault(date, false);
    }    
}
