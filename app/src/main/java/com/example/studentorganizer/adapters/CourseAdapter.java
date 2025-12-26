package com.example.studentorganizer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.studentorganizer.R;
import com.example.studentorganizer.models.Course;

import java.util.List;

public class CourseAdapter extends ArrayAdapter<Course> {

    private Context context;
    private List<Course> courses;

    public CourseAdapter(Context context, List<Course> courses) {
        super(context, 0, courses);
        this.context = context;
        this.courses = courses;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        }

        Course course = courses.get(position);

        TextView textViewCourseName = convertView.findViewById(R.id.textViewCourseName);
        TextView textViewCourseDetails = convertView.findViewById(R.id.textViewCourseDetails);

        textViewCourseName.setText(course.getName());
        String details = course.getDayName() + " • " + course.getStartTime() + "-" + course.getEndTime() +
                " • Prof: " + (course.getTeacher() != null ? course.getTeacher() : "N/A");
        textViewCourseDetails.setText(details);

        return convertView;
    }
}