package com.example.studentorganizer.models;

public class Exam {
    private int id;
    private int courseId;
    private String date;
    private String time;
    private String room;
    private Double grade;

    // Constructors
    public Exam() {}
    public Exam(int courseId, String date, String time, String room, Double grade) {
        this.courseId = courseId;
        this.date = date;
        this.time = time;
        this.room = room;
        this.grade = grade;
    }

    public Exam(int id, int courseId, String date, String time, String room, Double grade) {
        this(courseId, date, time, room, grade);
        this.id = id;
    }

    // Getters and Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public Double getGrade() { return grade; }
    public void setGrade(Double grade) { this.grade = grade; }

    @Override
    public String toString() {
        return date + " at " + time + " (Room: " + room + ")";
    }
}
