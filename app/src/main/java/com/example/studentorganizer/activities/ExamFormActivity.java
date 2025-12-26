package com.example.studentorganizer.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentorganizer.R;
import com.example.studentorganizer.db.CourseDAO;
import com.example.studentorganizer.db.ExamDAO;
import com.example.studentorganizer.models.Course;
import com.example.studentorganizer.models.Exam;

import java.util.ArrayList;
import java.util.List;
public class ExamFormActivity extends AppCompatActivity {
    private EditText editTextExamDate, editTextExamTime, editTextExamRoom, editTextExamGrade;
    private Spinner spinnerExamCourse;
    private Button btnSave, btnCancel;
    private ExamDAO examDAO;
    private CourseDAO courseDAO;
    private Exam exam;
    private int examId = -1;
    private List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_form);

        initializeUI();
        loadCourses();

        // Check for incoming intent data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            examId = extras.getInt("exam_id", -1);
            if (examId != -1) {
                // We are editing an existing exam
                setTitle("Edit Exam");
                loadExamData(examId);
            } else {
                // We are not editing, but a course might be pre-selected
                setTitle("Add Exam");
                int courseId = extras.getInt("course_id", -1);
                if (courseId != -1) {
                    selectCourseInSpinner(courseId);
                }
            }
        } else {
            // We are adding a new exam with no pre-selection
            setTitle("Add Exam");
        }
    }


    private void initializeUI() {
        editTextExamDate = findViewById(R.id.editTextExamDate);
        editTextExamTime = findViewById(R.id.editTextExamTime);
        editTextExamRoom = findViewById(R.id.editTextExamRoom);
        editTextExamGrade = findViewById(R.id.editTextExamGrade);
        spinnerExamCourse = findViewById(R.id.spinnerExamCourse);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        examDAO = new ExamDAO(this);
        courseDAO = new CourseDAO(this);

        btnSave.setOnClickListener(v -> saveExam());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadCourses() {
        courseList = courseDAO.getAllCourses();
        if (courseList == null) {
            courseList = new ArrayList<>(); // Ensure list is not null
            Toast.makeText(this, "Error: Could not load courses", Toast.LENGTH_LONG).show();
        }
        List<String> courseNames = new ArrayList<>();
        for(Course course : courseList) {
            courseNames.add(course.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, courseNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExamCourse.setAdapter(adapter);
    }

    private void selectCourseInSpinner(int courseId) {
        for(int i = 0; i < courseList.size(); i++) {
            if(courseList.get(i).getId() == courseId) {
                spinnerExamCourse.setSelection(i);
                break;
            }
        }
    }


    private void loadExamData(int examId) {
        exam = examDAO.getExamById(examId);
        if (exam != null) {
            editTextExamDate.setText(exam.getDate());
            editTextExamRoom.setText(exam.getRoom());
            editTextExamTime.setText(exam.getTime());
            if (exam.getGrade() != null) {
                editTextExamGrade.setText(String.valueOf(exam.getGrade()));
            }
            selectCourseInSpinner(exam.getCourseId());
        }
    }
    private void saveExam() {
        String date = editTextExamDate.getText().toString().trim();
        String time = editTextExamTime.getText().toString().trim();
        String room = editTextExamRoom.getText().toString().trim();
        String gradeStr = editTextExamGrade.getText().toString().trim();

        if (date.isEmpty() || spinnerExamCourse.getSelectedItemPosition() < 0) {
            Toast.makeText(this, "Please select a course and enter a date", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedCourseIndex = spinnerExamCourse.getSelectedItemPosition();
        int courseId = courseList.get(selectedCourseIndex).getId();

        Double grade = null;
        if(!gradeStr.isEmpty()) {
            try {
                grade = Double.parseDouble(gradeStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid grade format", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (examId == -1) {
            // New exam
            exam = new Exam(courseId, date, time, room, grade);
            examDAO.addExam(exam);
            Toast.makeText(this, "Exam added", Toast.LENGTH_SHORT).show();
        } else {
            // Update existing exam
            exam.setCourseId(courseId);
            exam.setDate(date);
            exam.setGrade(grade);
            exam.setRoom(room);
            exam.setTime(time);
            examDAO.updateExam(exam);
            Toast.makeText(this, "Exam updated", Toast.LENGTH_SHORT).show();
        }

        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        examDAO.close();
        courseDAO.close();
    }
}
