package com.example.studentorganizer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentorganizer.R;
import com.example.studentorganizer.adapters.CourseAdapter;
import com.example.studentorganizer.db.CourseDAO;
import com.example.studentorganizer.models.Course;

import java.util.List;


public class CourseListActivity extends AppCompatActivity {
    private ListView listViewCourses;
    private Button btnAddCourse, btnViewExams;
    private CourseDAO courseDAO;
    private CourseAdapter courseAdapter;
    private List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        initializeUI();
        loadCourses();
    }

    private void initializeUI() {
        listViewCourses = findViewById(R.id.listViewCourses);
        btnAddCourse = findViewById(R.id.btnAddCourse);
        btnViewExams = findViewById(R.id.btnViewExams);
        courseDAO = new CourseDAO(this);

        btnAddCourse.setOnClickListener(v -> {
            Intent intent = new Intent(CourseListActivity.this, CourseFormActivity.class);
            startActivity(intent);
        });

        btnViewExams.setOnClickListener(v -> {
            Intent intent = new Intent(CourseListActivity.this, ExamListActivity.class);
            startActivity(intent);
        });

        listViewCourses.setOnItemClickListener((parent, view, position, id) -> {
            Course selectedCourse = courseList.get(position);
            Intent intent = new Intent(CourseListActivity.this, CourseFormActivity.class);
            intent.putExtra("course_id", selectedCourse.getId());
            startActivity(intent);
        });

        listViewCourses.setOnItemLongClickListener((parent, view, position, id) -> {
            Course selectedCourse = courseList.get(position);
            courseDAO.deleteCourse(selectedCourse.getId());
            Toast.makeText(CourseListActivity.this, "Course deleted", Toast.LENGTH_SHORT).show();
            loadCourses();
            return true;
        });
    }

    private void loadCourses() {
        courseList = courseDAO.getAllCourses();
        courseAdapter = new CourseAdapter(this, courseList);
        listViewCourses.setAdapter(courseAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCourses();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        courseDAO.close();

    }

}
