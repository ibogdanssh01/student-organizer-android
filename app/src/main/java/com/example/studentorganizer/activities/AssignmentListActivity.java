package com.example.studentorganizer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentorganizer.R;
import com.example.studentorganizer.adapters.AssignmentAdapter;
import com.example.studentorganizer.db.AssignmentDAO;
import com.example.studentorganizer.db.CourseDAO;
import com.example.studentorganizer.models.Assignment;

import java.util.List;

public class AssignmentListActivity extends AppCompatActivity {

    private ListView listViewAssignments;
    private Button btnAddAssignment;
    private AssignmentDAO assignmentDAO;
    private CourseDAO courseDAO;
    private AssignmentAdapter assignmentAdapter;
    private List<Assignment> assignmentList;
    private int courseId = -1;  // Optional: filtrare pe curs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_list);

        initializeUI();
        loadAssignments();
    }

    private void initializeUI() {
        listViewAssignments = findViewById(R.id.listViewAssignments);
        btnAddAssignment = findViewById(R.id.btnAddAssignment);
        assignmentDAO = new AssignmentDAO(this);
        courseDAO = new CourseDAO(this);

        // Optional: primeste courseId daca vii din CourseListActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            courseId = extras.getInt("course_id", -1);
        }

        btnAddAssignment.setOnClickListener(v -> {
            Intent intent = new Intent(AssignmentListActivity.this, AssignmentFormActivity.class);
            if (courseId != -1) {
                intent.putExtra("course_id", courseId);
            }
            startActivity(intent);
        });

        listViewAssignments.setOnItemClickListener((parent, view, position, id) -> {
            Assignment selectedAssignment = assignmentList.get(position);
            Intent intent = new Intent(AssignmentListActivity.this, AssignmentFormActivity.class);
            intent.putExtra("assignment_id", selectedAssignment.getId());
            startActivity(intent);
        });

        listViewAssignments.setOnItemLongClickListener((parent, view, position, id) -> {
            Assignment selectedAssignment = assignmentList.get(position);
            assignmentDAO.deleteAssignment(selectedAssignment.getId());
            Toast.makeText(AssignmentListActivity.this, "Assignment deleted", Toast.LENGTH_SHORT).show();
            loadAssignments();
            return true;
        });
    }

    private void loadAssignments() {
        if (courseId != -1) {
            assignmentList = assignmentDAO.getAssignmentsByCourse(courseId);
        } else {
            assignmentList = assignmentDAO.getAllAssignments();
        }
        assignmentAdapter = new AssignmentAdapter(this, assignmentList, courseDAO);
        listViewAssignments.setAdapter(assignmentAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAssignments();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        assignmentDAO.close();
        courseDAO.close();
    }
}
