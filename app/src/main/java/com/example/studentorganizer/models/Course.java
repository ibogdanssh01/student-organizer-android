package com.example.studentorganizer.models;

public class Course {
    private int id;
    private String name;
    private String teacher;
    private String room;
    private int dayOfWeek; // 0-4(Monday-Friday)
    private String startTime; // "09:00"
    private String endTime; // "11:00"

    // constructori
    public Course() {}
    public Course(String name, String teacher, String room, int dayOfWeek, String startTime, String endTime) {
        this.name = name;
        this.teacher = teacher;
        this.room = room;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Course(int id, String name, String teacher, String room, int dayOfWeek, String startTime, String endTime) {
        this(name, teacher, room, dayOfWeek, startTime, endTime);
        this.id = id;
    }

    // Getters Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTeacher() { return teacher; }
    public void setTeacher(String teacher) { this.teacher = teacher; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public int getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(int dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getDayName() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        return dayOfWeek >= 0 && dayOfWeek < days.length ? days[dayOfWeek] : "Unknown";
    }

    @Override
    public String toString() {
        return name + " (" + getDayName() + ", " + startTime + "-" + endTime + ")";
    }
}
