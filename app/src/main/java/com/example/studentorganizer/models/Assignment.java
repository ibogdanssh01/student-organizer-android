package com.example.studentorganizer.models;

public class Assignment {
    private int id;
    private int courseId;
    private String title;
    private String description;
    private String deadline; // "10-01-2025"
    private String status; // "TODO", "IN_PROGRESS", "DONE"

    // Constructori
    public Assignment() {}
    public Assignment(int courseId, String title, String description, String deadline, String status) {
        this.courseId = courseId;
        this.title = title;
        this.deadline = deadline;
        this.description = description;
        this.status = status;
    }

    public Assignment(int id, int courseId, String title, String description, String deadline, String status) {
        this(courseId, title, description, deadline, status);
        this.id = id;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    @Override
    public String toString() {
        return title + " [" + status + "] - Due: " + deadline;
    }





}
