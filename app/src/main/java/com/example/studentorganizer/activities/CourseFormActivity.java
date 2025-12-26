package com.example.studentorganizer.activities;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentorganizer.R;
import com.example.studentorganizer.db.CourseDAO;
import com.example.studentorganizer.models.Course;
public class CourseFormActivity extends AppCompatActivity {
    private EditText editTextCourseName, editTextTeacher, editTextRoom, editTextStartTime, editTextEndTime;
    private Spinner spinnerDayOfWeek;
    private Button btnSave, btnCancel;
    private CourseDAO courseDAO;
    private Course course;
    private int courseId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_form);

        initializeUI();
        setupDaySpinner();

        // Check if editing existing course
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            courseId = extras.getInt("course_id", -1);
            if (courseId != -1) {
                loadCourseData(courseId);
            }
        }
    }


    private void initializeUI() {
        editTextCourseName = findViewById(R.id.editTextCourseName);
        editTextTeacher = findViewById(R.id.editTextTeacher);
        editTextRoom = findViewById(R.id.editTextRoom);
        editTextStartTime = findViewById(R.id.editTextStartTime);
        editTextEndTime = findViewById(R.id.editTextEndTime);
        spinnerDayOfWeek = findViewById(R.id.spinnerDayOfWeek);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        courseDAO = new CourseDAO(this);

        btnSave.setOnClickListener(v -> saveCourse());
        btnCancel.setOnClickListener(v -> finish());

    }

    private void setupDaySpinner() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDayOfWeek.setAdapter(adapter);
    }

    private void loadCourseData(int courseId) {
        course = courseDAO.getCourseById(courseId);
        if (course != null) {
            editTextCourseName.setText(course.getName());
            editTextTeacher.setText(course.getTeacher());
            editTextRoom.setText(course.getRoom());
            editTextStartTime.setText(course.getStartTime());
            editTextEndTime.setText(course.getEndTime());
            spinnerDayOfWeek.setSelection(course.getDayOfWeek());
        }
    }

    private void saveCourse() {
        String name = editTextCourseName.getText().toString().trim();
        String teacher = editTextTeacher.getText().toString().trim();
        String room = editTextRoom.getText().toString().trim();
        String startTime = editTextStartTime.getText().toString().trim();
        String endTime = editTextEndTime.getText().toString().trim();
        int dayOfWeek = spinnerDayOfWeek.getSelectedItemPosition();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter course name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (courseId == -1) {
            // new course
            course = new Course(name, teacher, room, dayOfWeek, startTime, endTime);
            courseDAO.addCourse(course);
            Toast.makeText(this, "Course added", Toast.LENGTH_SHORT).show();
        } else {
            // Update existing course
            course.setName(name);
            course.setTeacher(teacher);
            course.setRoom(room);
            course.setDayOfWeek(dayOfWeek);
            course.setStartTime(startTime);
            course.setEndTime(endTime);
            courseDAO.updateCourse(course);
            Toast.makeText(this, "Course updated", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        courseDAO.close();
    }
}
