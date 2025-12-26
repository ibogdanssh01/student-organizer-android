package com.example.studentorganizer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentorganizer.R;
import com.example.studentorganizer.adapters.ExamAdapter;
import com.example.studentorganizer.db.CourseDAO;
import com.example.studentorganizer.db.ExamDAO;
import com.example.studentorganizer.models.Exam;

import java.util.List;
public class ExamListActivity extends AppCompatActivity {
    private ListView listViewExams;
    private Button btnAddExam;
    private Button btnBackToCourses;
    private ExamDAO examDAO;
    private CourseDAO courseDAO;
    private ExamAdapter examAdapter;
    private List<Exam> examList;
    private int courseId = -1; // Course filtration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_list);

        initializeUI();
        loadExams();
    }

    private void initializeUI() {
        listViewExams = findViewById(R.id.listViewExams);
        btnAddExam = findViewById(R.id.btnAddExam);
        examDAO = new ExamDAO(this);
        courseDAO = new CourseDAO(this);
        btnBackToCourses = findViewById(R.id.btnBackToCourses);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            courseId = extras.getInt("course_id", -1);
        }

        btnAddExam.setOnClickListener(v -> {
            Intent intent = new Intent(ExamListActivity.this, ExamFormActivity.class);
            if (courseId != -1) {
                intent.putExtra("course_id", courseId);
            }
            startActivity(intent);
        });


        listViewExams.setOnItemClickListener((parent, view, position, id) -> {
            Exam selectedExam = examList.get(position);
            Intent intent = new Intent(ExamListActivity.this, ExamFormActivity.class);
            intent.putExtra("exam_id", selectedExam.getId());
            startActivity(intent);
        });

        listViewExams.setOnItemLongClickListener((parent, view, position, id) -> {
            Exam selectedExam = examList.get(position);
            examDAO.deleteExam(selectedExam.getId());
            Toast.makeText(ExamListActivity.this, "Exam deleted", Toast.LENGTH_SHORT).show();
            loadExams();
            return true;
        });

        btnBackToCourses.setOnClickListener(v -> {
            Intent intent = new Intent(ExamListActivity.this, CourseListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

    }

    private void loadExams() {
        if (courseId != -1) {
            examList = examDAO.getExamByCourse(courseId);
        } else {
            examList = examDAO.getAllExams();
        }

        examAdapter = new ExamAdapter(this, examList, courseDAO);
        listViewExams.setAdapter(examAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExams();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        examDAO.close();
        courseDAO.close();
    }

}
