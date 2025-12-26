package com.example.studentorganizer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.studentorganizer.R;
import com.example.studentorganizer.db.CourseDAO;
import com.example.studentorganizer.models.Assignment;
import com.example.studentorganizer.models.Course;

import java.util.List;

public class AssignmentAdapter extends ArrayAdapter<Assignment> {

    private Context context;
    private List<Assignment> assignments;
    private CourseDAO courseDAO;

    public AssignmentAdapter(Context context, List<Assignment> assignments, CourseDAO courseDAO) {
        super(context, 0, assignments);
        this.context = context;
        this.assignments = assignments;
        this.courseDAO = courseDAO;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_assignment, parent, false);
        }

        Assignment assignment = assignments.get(position);
        Course course = courseDAO.getCourseById(assignment.getCourseId());

        TextView textViewAssignmentTitle = convertView.findViewById(R.id.textViewAssignmentTitle);
        TextView textViewAssignmentCourse = convertView.findViewById(R.id.textViewAssignmentCourse);
        TextView textViewAssignmentDeadline = convertView.findViewById(R.id.textViewAssignmentDeadline);
        TextView textViewAssignmentStatus = convertView.findViewById(R.id.textViewAssignmentStatus);

        textViewAssignmentTitle.setText(assignment.getTitle());
        textViewAssignmentCourse.setText(course != null ? course.getName() : "Unknown Course");
        textViewAssignmentDeadline.setText("Due: " + assignment.getDeadline());
        textViewAssignmentStatus.setText("Status: " + assignment.getStatus());

        // Colorare după status
        int statusColor = getStatusColor(assignment.getStatus());
        textViewAssignmentStatus.setTextColor(statusColor);

        return convertView;
    }

    private int getStatusColor(String status) {
        if (status.equals("DONE")) {
            return 0xFF4CAF50;  // Verde
        } else if (status.equals("IN_PROGRESS")) {
            return 0xFFFFA500;  // Portocaliu
        } else {  // TODO
            return 0xFFFF5252;  // Roșu
        }
    }
}
