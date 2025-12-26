package com.example.studentorganizer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentorganizer.R;

public class MainActivity extends AppCompatActivity {

    private Button btnViewCourses;
    private Button btnViewAssignments;
    private Button btnViewExams;
    private Button btnSchedule;
    private Button btnDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeButtons();
    }

    private void initializeButtons() {
        btnViewCourses = findViewById(R.id.btnViewCourses);
        btnViewAssignments = findViewById(R.id.btnViewAssignments);
        btnViewExams = findViewById(R.id.btnViewExams);
        btnSchedule = findViewById(R.id.btnSchedule);
        btnDashboard = findViewById(R.id.btnDashboard);

        // Courses
        btnViewCourses.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CourseListActivity.class);
            startActivity(intent);
        });

        // Assignments
        btnViewAssignments.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AssignmentListActivity.class);
            startActivity(intent);
        });

        // Exams
        btnViewExams.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExamListActivity.class);
            startActivity(intent);
        });

        // Schedule
        btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
            startActivity(intent);
        });

        // Dashboard
        btnDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
        });
    }
}
