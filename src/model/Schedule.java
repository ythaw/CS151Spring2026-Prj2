package model;

import java.util.HashSet;
import java.util.Set;

public class Schedule {
    // Days when the class meets, such as Mon, Tue, Wed
    private Set<String> days;

    // Start time in total minutes from 00:00
    private int startMinutes;

    // End time in total minutes from 00:00
    private int endMinutes;

    // Classroom or meeting location
    private String location;

    // Constructor to create a schedule
    public Schedule(Set<String> days, int startMinutes, int endMinutes, String location) {
		 if (days == null || days.isEmpty()) {
            throw new IllegalArgumentException("Days cannot be empty.");
        }
        if (startMinutes < 0) {
            throw new IllegalArgumentException("Start time cannot be negative.");
        }
        if (endMinutes <= startMinutes) {
            throw new IllegalArgumentException("End time must be after start time.");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty.");
        }
        
        this.days = new HashSet<>(days);
        this.startMinutes = startMinutes;
        this.endMinutes = endMinutes;
        this.location = location;
    }

    // Check whether this schedule overlaps with another schedule
    public boolean overlaps(Schedule other) {
        if (other == null) {
            return false;
        }

        // First check if the two schedules share at least one day
        boolean sharedDay = false;
        for (String day : days) {
            if (other.days.contains(day)) {
                sharedDay = true;
                break;
            }
        }

        // If no shared day, they cannot overlap
        if (!sharedDay) {
            return false;
        }

        // Time overlap condition
        return startMinutes < other.endMinutes && endMinutes > other.startMinutes;
    }

    // Check if the schedule data is valid
    public boolean isValid() {
        return !days.isEmpty() && startMinutes >= 0 && endMinutes > startMinutes;
    }

    // Return total class duration in minutes
    public int durationMinutes() {
        return endMinutes - startMinutes;
    }

    // Return a readable string for display
    public String toDisplayString() {
        return days + " " + formatTime(startMinutes) + " - " + formatTime(endMinutes) + " @ " + location;
    }

    // Check if the schedule happens on a specific day
    public boolean occursOnDay(String day) {
        return days.contains(day);
    }

    // Helper method to convert minutes into HH:MM format
    private String formatTime(int totalMinutes) {
        int hour = totalMinutes / 60;
        int minute = totalMinutes % 60;
        return String.format("%02d:%02d", hour, minute);
    }

    // Getter for days
    public Set<String> getDays() {
        return days;
    }

    // Setter for days
    public void setDays(Set<String> days) {
        if (days != null && !days.isEmpty()) {
            this.days = new HashSet<>(days);
        }
    }

    // Getter for startMinutes
    public int getStartMinutes() {
        return startMinutes;
    }

    // Setter for startMinutes
    public void setStartMinutes(int startMinutes) {
        this.startMinutes = startMinutes;
    }

    // Getter for endMinutes
    public int getEndMinutes() {
        return endMinutes;
    }

    // Setter for endMinutes
    public void setEndMinutes(int endMinutes) {
        this.endMinutes = endMinutes;
    }

    // Getter for location
    public String getLocation() {
        return location;
    }

    // Setter for location
    public void setLocation(String location) {
        if (location != null && !location.isBlank()) {
            this.location = location;
        }
    }

    // Return display string when object is printed
    @Override
    public String toString() {
        return toDisplayString();
    }
}
