package com.example.studentorganizer.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentorganizer.R;
import com.example.studentorganizer.db.AssignmentDAO;
import com.example.studentorganizer.db.CourseDAO;
import com.example.studentorganizer.models.Assignment;
import com.example.studentorganizer.models.Course;

import java.util.ArrayList;
import java.util.List;

public class AssignmentFormActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextDeadline;
    private Spinner spinnerAssignmentCourse, spinnerAssignmentStatus;
    private Button btnSave, btnCancel;
    private AssignmentDAO assignmentDAO;
    private CourseDAO courseDAO;
    private Assignment assignment;
    private int assignmentId = -1;
    private List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_form);

        initializeUI();
        loadCourses();
        setupStatusSpinner();

        // Check if editing existing assignment
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            assignmentId = extras.getInt("assignment_id", -1);
            if (assignmentId != -1) {
                loadAssignmentData(assignmentId);
            } else {
                // Poate venim din CourseListActivity cu course_id pre-selectat
                int courseId = extras.getInt("course_id", -1);
                if (courseId != -1) {
                    selectCourseInSpinner(courseId);
                }
            }
        }
    }

    private void initializeUI() {
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDeadline = findViewById(R.id.editTextDeadline);
        spinnerAssignmentCourse = findViewById(R.id.spinnerAssignmentCourse);
        spinnerAssignmentStatus = findViewById(R.id.spinnerAssignmentStatus);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        assignmentDAO = new AssignmentDAO(this);
        courseDAO = new CourseDAO(this);

        btnSave.setOnClickListener(v -> saveAssignment());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadCourses() {
        courseList = courseDAO.getAllCourses();
        List<String> courseNames = new ArrayList<>();
        for (Course course : courseList) {
            courseNames.add(course.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, courseNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssignmentCourse.setAdapter(adapter);
    }

    private void setupStatusSpinner() {
        String[] statuses = {"TODO", "IN_PROGRESS", "DONE"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssignmentStatus.setAdapter(adapter);
    }

    private void selectCourseInSpinner(int courseId) {
        for (int i = 0; i < courseList.size(); i++) {
            if (courseList.get(i).getId() == courseId) {
                spinnerAssignmentCourse.setSelection(i);
                break;
            }
        }
    }

    private void loadAssignmentData(int assignmentId) {
        assignment = assignmentDAO.getAssignmentById(assignmentId);
        if (assignment != null) {
            editTextTitle.setText(assignment.getTitle());
            editTextDescription.setText(assignment.getDescription());
            editTextDeadline.setText(assignment.getDeadline());
            selectCourseInSpinner(assignment.getCourseId());

            // Selecteaza status din spinner
            String[] statuses = {"TODO", "IN_PROGRESS", "DONE"};
            for (int i = 0; i < statuses.length; i++) {
                if (statuses[i].equals(assignment.getStatus())) {
                    spinnerAssignmentStatus.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveAssignment() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String deadline = editTextDeadline.getText().toString().trim();

        if (title.isEmpty() || deadline.isEmpty() || spinnerAssignmentCourse.getSelectedItemPosition() == -1) {
            Toast.makeText(this, "Please fill in required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedCourseIndex = spinnerAssignmentCourse.getSelectedItemPosition();
        int courseId = courseList.get(selectedCourseIndex).getId();
        String status = (String) spinnerAssignmentStatus.getSelectedItem();

        if (assignmentId == -1) {
            // New assignment
            assignment = new Assignment(courseId, title, description, deadline, status);
            assignmentDAO.addAssignment(assignment);
            Toast.makeText(this, "Assignment added", Toast.LENGTH_SHORT).show();
        } else {
            // Update existing assignment
            assignment.setCourseId(courseId);
            assignment.setTitle(title);
            assignment.setDescription(description);
            assignment.setDeadline(deadline);
            assignment.setStatus(status);
            assignmentDAO.updateAssignment(assignment);
            Toast.makeText(this, "Assignment updated", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        assignmentDAO.close();
        courseDAO.close();
    }
}
