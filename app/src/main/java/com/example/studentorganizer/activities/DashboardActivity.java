package com.example.studentorganizer.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentorganizer.R;
import com.example.studentorganizer.db.CourseDAO;
import com.example.studentorganizer.db.ExamDAO;
import com.example.studentorganizer.db.AssignmentDAO;
import com.example.studentorganizer.models.Course;
import com.example.studentorganizer.models.Exam;
import com.example.studentorganizer.models.Assignment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    private CourseDAO courseDAO;
    private ExamDAO examDAO;
    private AssignmentDAO assignmentDAO;
    private LinearLayout dashboardContainer;
    private TextView textViewOverallGPA;
    private TextView textViewStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initializeUI();
        loadDashboardData();
    }

    private void initializeUI() {
        dashboardContainer = findViewById(R.id.dashboardContainer);
        textViewOverallGPA = findViewById(R.id.textViewOverallGPA);
        textViewStatistics = findViewById(R.id.textViewStatistics);

        courseDAO = new CourseDAO(this);
        examDAO = new ExamDAO(this);
        assignmentDAO = new AssignmentDAO(this);

    }

    private void loadDashboardData() {
        dashboardContainer.removeAllViews();

        // Load all data
        List<Course> courses = courseDAO.getAllCourses();
        List<Exam> exams = examDAO.getAllExams();
        List<Assignment> assignments = assignmentDAO.getAllAssignments();

        // Calculate GPA
        double overallGPA = calculateOverallGPA(exams, courses);
        displayOverallGPA(overallGPA);

        // Display statistics
        displayGeneralStatistics(courses, exams, assignments);

        // Display per-course GPA
        displayPerCourseGPA(exams, courses);

        // Display assignment statistics
        displayAssignmentStatistics(assignments);
    }

    private double calculateOverallGPA(List<Exam> exams, List<Course> courses) {
        if (exams.isEmpty()) {
            return 0.0;
        }

        double totalGrade = 0;
        int countGraded = 0;

        for (Exam exam : exams) {
            if (exam.getGrade() != null && exam.getGrade() > 0) {
                totalGrade += exam.getGrade();
                countGraded++;
            }
        }

        return countGraded > 0 ? totalGrade / countGraded : 0.0;
    }

    private Map<Integer, Double> calculatePerCourseGPA(List<Exam> exams) {
        Map<Integer, Double> courseGPA = new HashMap<>();
        Map<Integer, Integer> courseCount = new HashMap<>();

        for (Exam exam : exams) {
            if (exam.getGrade() != null && exam.getGrade() > 0) {
                int courseId = exam.getCourseId();
                double currentGrade = courseGPA.getOrDefault(courseId, 0.0);
                int count = courseCount.getOrDefault(courseId, 0);

                courseGPA.put(courseId, currentGrade + exam.getGrade());
                courseCount.put(courseId, count + 1);
            }
        }

        // Calculate averages
        for (Integer courseId : courseGPA.keySet()) {
            double total = courseGPA.get(courseId);
            int count = courseCount.get(courseId);
            courseGPA.put(courseId, total / count);
        }

        return courseGPA;
    }

    private void displayOverallGPA(double gpa) {
        textViewOverallGPA.setText(String.format("Overall GPA: %.2f / 10.0", gpa));

        // Color based on GPA
        int textColor;
        if (gpa >= 8.5) {
            textColor = 0xFF4CAF50; // Green - Excellent
        } else if (gpa >= 7.0) {
            textColor = 0xFF2196F3; // Blue - Good
        } else if (gpa >= 5.0) {
            textColor = 0xFFFFC107; // Amber - Fair
        } else {
            textColor = 0xFFF44336; // Red - Poor
        }

        textViewOverallGPA.setTextColor(textColor);
        textViewOverallGPA.setTextSize(22);
    }

    private void displayGeneralStatistics(List<Course> courses, List<Exam> exams, List<Assignment> assignments) {
        LinearLayout statsCard = createCard("General Statistics");

        // Total courses
        TextView coursesText = new TextView(this);
        coursesText.setText("Total Courses: " + courses.size());
        coursesText.setTextSize(14);
        coursesText.setPadding(8, 8, 8, 4);
        statsCard.addView(coursesText);

        // Total exams
        TextView examsText = new TextView(this);
        examsText.setText("Total Exams: " + exams.size());
        examsText.setTextSize(14);
        examsText.setPadding(8, 4, 8, 4);
        statsCard.addView(examsText);

        // Graded exams
        long gradedExams = exams.stream()
                .filter(e -> e.getGrade() != null && e.getGrade() > 0)
                .count();
        TextView gradedExamsText = new TextView(this);
        gradedExamsText.setText("Graded Exams: " + gradedExams + " / " + exams.size());
        gradedExamsText.setTextSize(14);
        gradedExamsText.setPadding(8, 4, 8, 4);
        statsCard.addView(gradedExamsText);

        // Total assignments
        TextView assignmentsText = new TextView(this);
        assignmentsText.setText("Total Assignments: " + assignments.size());
        assignmentsText.setTextSize(14);
        assignmentsText.setPadding(8, 4, 8, 4);
        statsCard.addView(assignmentsText);

        // Completed assignments
        long completedAssignments = assignments.stream()
                .filter(a -> a.getStatus().equals("DONE"))
                .count();
        TextView completedText = new TextView(this);
        completedText.setText("Completed: " + completedAssignments + " / " + assignments.size());
        completedText.setTextSize(14);
        completedText.setPadding(8, 4, 8, 8);
        completedText.setTextColor(0xFF4CAF50);
        statsCard.addView(completedText);

        dashboardContainer.addView(statsCard);
    }

    private void displayPerCourseGPA(List<Exam> exams, List<Course> courses) {
        if (exams.isEmpty()) {
            return;
        }

        LinearLayout gpaCard = createCard("GPA per Course");

        Map<Integer, Double> courseGPA = calculatePerCourseGPA(exams);

        if (courseGPA.isEmpty()) {
            TextView noDataText = new TextView(this);
            noDataText.setText("No graded exams yet");
            noDataText.setTextSize(12);
            noDataText.setTextColor(0xFF999999);
            noDataText.setPadding(8, 8, 8, 8);
            gpaCard.addView(noDataText);
        } else {
            for (Map.Entry<Integer, Double> entry : courseGPA.entrySet()) {
                int courseId = entry.getKey();
                double gpa = entry.getValue();

                // Find course name
                String courseName = "Unknown Course";
                for (Course course : courses) {
                    if (course.getId() == courseId) {
                        courseName = course.getName();
                        break;
                    }
                }

                LinearLayout courseRow = new LinearLayout(this);
                courseRow.setOrientation(LinearLayout.HORIZONTAL);
                courseRow.setPadding(8, 4, 8, 4);

                TextView courseNameText = new TextView(this);
                courseNameText.setText(courseName);
                courseNameText.setTextSize(13);
                courseNameText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f));
                courseRow.addView(courseNameText);

                TextView gpaText = new TextView(this);
                gpaText.setText(String.format("%.2f", gpa));
                gpaText.setTextSize(13);
                gpaText.setTextColor(0xFF2196F3);
                gpaText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f));
                courseRow.addView(gpaText);

                gpaCard.addView(courseRow);
            }
        }

        dashboardContainer.addView(gpaCard);
    }

    private void displayAssignmentStatistics(List<Assignment> assignments) {
        if (assignments.isEmpty()) {
            return;
        }

        LinearLayout assignCard = createCard("Assignment Status");

        long todoCount = assignments.stream()
                .filter(a -> a.getStatus().equals("TODO"))
                .count();
        long inProgressCount = assignments.stream()
                .filter(a -> a.getStatus().equals("IN_PROGRESS"))
                .count();
        long doneCount = assignments.stream()
                .filter(a -> a.getStatus().equals("DONE"))
                .count();

        // TODO
        LinearLayout todoRow = createStatusRow("TODO", todoCount, 0xFFFF5252);
        assignCard.addView(todoRow);

        // IN_PROGRESS
        LinearLayout inProgressRow = createStatusRow("In Progress", inProgressCount, 0xFFFFA500);
        assignCard.addView(inProgressRow);

        // DONE
        LinearLayout doneRow = createStatusRow("Done", doneCount, 0xFF4CAF50);
        assignCard.addView(doneRow);

        // Completion percentage
        double completionPercentage = (doneCount / (double) assignments.size()) * 100;
        TextView completionText = new TextView(this);
        completionText.setText(String.format("Completion: %.1f%%", completionPercentage));
        completionText.setTextSize(12);
        completionText.setPadding(8, 8, 8, 4);
        completionText.setTextColor(0xFF2196F3);
        assignCard.addView(completionText);

        dashboardContainer.addView(assignCard);
    }

    private LinearLayout createStatusRow(String label, long count, int color) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(8, 4, 8, 4);

        TextView labelText = new TextView(this);
        labelText.setText(label + ":");
        labelText.setTextSize(12);
        labelText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f));
        row.addView(labelText);

        TextView countText = new TextView(this);
        countText.setText(String.valueOf(count));
        countText.setTextSize(12);
        countText.setTextColor(color);
        countText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f));
        row.addView(countText);

        return row;
    }

    private LinearLayout createCard(String title) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 8, 0, 8);
        card.setLayoutParams(cardParams);
        card.setBackgroundColor(0xFFF5F5F5);
        card.setPadding(12, 12, 12, 12);

        // Title
        TextView titleText = new TextView(this);
        titleText.setText(title);
        titleText.setTextSize(16);
        titleText.setTypeface(null, android.graphics.Typeface.BOLD);
        titleText.setTextColor(0xFF2196F3);
        titleText.setPadding(0, 0, 0, 8);
        card.addView(titleText);

        return card;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        courseDAO.close();
        examDAO.close();
        assignmentDAO.close();
    }
}
