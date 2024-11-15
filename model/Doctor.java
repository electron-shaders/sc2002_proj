package model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Doctor extends User implements Comparable<Doctor> {
    private String specialty;
    private int ratingCount;
    private float rating;
    private Map<Date, Boolean> availability;

    public Doctor(String userId, String password, UserRole role, String name, boolean isMale, int age, String email, String specialty, int ratingCount, float rating) {
        // constructor logic
        super(userId, password, role, name, isMale, age, email);
        this.specialty = specialty;
        this.ratingCount = ratingCount;
        this.rating = rating;
        this.availability = new HashMap<Date, Boolean>();
    }

    public int compareTo(Doctor o) {
        float diff = this.rating - o.rating;
        if (diff > 0)
            return -1;
        else if (diff < 0)
            return 1;
        else
            return 0;
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
