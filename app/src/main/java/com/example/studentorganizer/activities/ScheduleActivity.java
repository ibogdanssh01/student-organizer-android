package com.example.studentorganizer.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentorganizer.R;
import com.example.studentorganizer.db.CourseDAO;
import com.example.studentorganizer.models.Course;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ScheduleActivity extends AppCompatActivity {

    private CourseDAO courseDAO;
    private LinearLayout scheduleContainer;
//    private Button btnBackToMain;
    private TextView textViewNoSchedule;

    private static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private Map<String, List<Course>> coursesByDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        initializeUI();
        loadSchedule();
    }

    private void initializeUI() {
        scheduleContainer = findViewById(R.id.scheduleContainer);
//        btnBackToMain = findViewById(R.id.btnBackToMain);
        textViewNoSchedule = findViewById(R.id.textViewNoSchedule);

        courseDAO = new CourseDAO(this);
        coursesByDay = new HashMap<>();

//        btnBackToMain.setOnClickListener(v -> finish());
    }

    private void loadSchedule() {
        List<Course> allCourses = courseDAO.getAllCourses();

        for (String day : DAYS) {
            coursesByDay.put(day, new ArrayList<>());
        }

        for (Course course : allCourses) {
            String day = course.getDayName();
            if (coursesByDay.containsKey(day)) {
                Objects.requireNonNull(coursesByDay.get(day)).add(course);
            }
        }

        for (String day : DAYS) {
            List<Course> courses = coursesByDay.get(day);
            assert courses != null;
            courses.sort(Comparator.comparing(Course::getStartTime));
        }

        // Verify if we have courses
        boolean hasAnyCourse = !allCourses.isEmpty();

        if (hasAnyCourse) {
            textViewNoSchedule.setVisibility(TextView.GONE);
            displayScheduleTable();
        } else {
            textViewNoSchedule.setVisibility(TextView.VISIBLE);
            scheduleContainer.removeAllViews();
        }
    }

    private void displayScheduleTable() {
        scheduleContainer.removeAllViews();

        for (String day : DAYS) {
            List<Course> coursesForDay = coursesByDay.get(day);


            assert coursesForDay != null;
            LinearLayout dayCard = createDayCard(day, coursesForDay);
            scheduleContainer.addView(dayCard);
        }
    }

    @SuppressLint("SetTextI18n")
    private LinearLayout createDayCard(String day, List<Course> courses) {
        LinearLayout dayCard = getLinearLayout();

        // Day header
        TextView dayHeader = new TextView(this);
        dayHeader.setText(day);
        dayHeader.setTextSize(18);
        dayHeader.setTypeface(null, android.graphics.Typeface.BOLD);
        dayHeader.setTextColor(0xFF2196F3);
        dayHeader.setPadding(8, 8, 8, 12);
        dayCard.addView(dayHeader);

        if (courses.isEmpty()) {
            TextView noCourses = new TextView(this);
            noCourses.setText("No courses");
            noCourses.setTextSize(14);
            noCourses.setTextColor(0xFF999999);
            noCourses.setPadding(8, 8, 8, 8);
            dayCard.addView(noCourses);
        } else {
            // Show every course
            for (Course course : courses) {
                LinearLayout courseRow = createCourseRow(course);
                dayCard.addView(courseRow);
            }
        }

        return dayCard;
    }

    @NonNull
    private LinearLayout getLinearLayout() {
        LinearLayout dayCard = new LinearLayout(this);
        dayCard.setOrientation(LinearLayout.VERTICAL);
        dayCard.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        dayCard.setPadding(16, 16, 16, 16);
        dayCard.setBackgroundColor(0xFFF5F5F5);

        // Sep for days
        LinearLayout.LayoutParams marginParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        marginParams.setMargins(0, 8, 0, 8);
        dayCard.setLayoutParams(marginParams);
        return dayCard;
    }

    @SuppressLint("SetTextI18n")
    private LinearLayout createCourseRow(Course course) {
        LinearLayout row = getLayout();

        // Time
        TextView timeText = new TextView(this);
        timeText.setText(course.getStartTime() + " - " + course.getEndTime());
        timeText.setTextSize(12);
        timeText.setTypeface(null, android.graphics.Typeface.BOLD);
        timeText.setTextColor(0xFF2196F3);
        timeText.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.3f
        ));
        timeText.setPadding(8, 0, 8, 0);
        row.addView(timeText);

        // Course details
        LinearLayout courseDetails = new LinearLayout(this);
        courseDetails.setOrientation(LinearLayout.VERTICAL);
        courseDetails.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.7f
        ));

        // Course title
        TextView courseTitle = new TextView(this);
        courseTitle.setText(course.getName());
        courseTitle.setTextSize(14);
        courseTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        courseTitle.setTextColor(0xFF000000);
        courseDetails.addView(courseTitle);

        // Teacher
        TextView professor = new TextView(this);
        professor.setText("Teacher: " + course.getTeacher());
        professor.setTextSize(12);
        professor.setTextColor(0xFF666666);
        courseDetails.addView(professor);

        // Room
        TextView room = new TextView(this);
        room.setText("Room: " + course.getRoom());
        room.setTextSize(12);
        room.setTextColor(0xFF999999);
        courseDetails.addView(room);

        row.addView(courseDetails);

        return row;
    }

    @NonNull
    private LinearLayout getLayout() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        row.setPadding(8, 8, 8, 8);
        row.setBackgroundColor(0xFFFFFFFF);

        // Margin after every row
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rowParams.setMargins(0, 4, 0, 4);
        row.setLayoutParams(rowParams);
        return row;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        courseDAO.close();
    }
}
