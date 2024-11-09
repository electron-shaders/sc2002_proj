package model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Doctor extends User implements Comparable<Doctor> {
    private String specialty;
    private int ratingCount;
    private float rating;
    private HashMap<Date, Boolean> availability;

    public Doctor(String userId, String password, UserRole role, String name, boolean isMale, int age, String email, String specialty, int ratingCount, float rating, HashMap<Date, Boolean> availability) {
        // constructor logic
        super(userId, password, role, name, isMale, 0, email);
        this.specialty = specialty;
        this.ratingCount = ratingCount;
        this.rating = rating;
        this.availability = availability;
    }

    public int compareTo(Doctor o) {
        if (this.rating - o.rating == 0)
            return 0;
        else if (this.rating - o.rating > 0)
            return 1;
        else
            return -1;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void incrementRatingCount() {
        ratingCount++;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Map<Date, Boolean> getAvailability() {
        return availability;
    }

    public void addAvailability(Date date) {
        availability.put(date, true);
    }

    public void removeAvailability(Date date) {
        availability.put(date, false);
    }

    public boolean isAvailable(Date date) {
        return availability.getOrDefault(date, false);
    }

    
}
