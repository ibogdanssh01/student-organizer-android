package com.example.studentorganizer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.studentorganizer.R;
import com.example.studentorganizer.db.CourseDAO;
import com.example.studentorganizer.models.Course;
import com.example.studentorganizer.models.Exam;

import java.util.List;

public class ExamAdapter extends ArrayAdapter<Exam> {

    private Context context;
    private List<Exam> exams;
    private CourseDAO courseDAO;

    public ExamAdapter(Context context, List<Exam> exams, CourseDAO courseDAO) {
        super(context, 0, exams);
        this.context = context;
        this.exams = exams;
        this.courseDAO = courseDAO;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_exam, parent, false);
        }

        Exam exam = exams.get(position);
        Course course = courseDAO.getCourseById(exam.getCourseId());

        TextView textViewExamCourse = convertView.findViewById(R.id.textViewExamCourse);
        TextView textViewExamDate = convertView.findViewById(R.id.textViewExamDate);
        TextView textViewExamGrade = convertView.findViewById(R.id.textViewExamGrade);

        textViewExamCourse.setText(course != null ? course.getName() : "Unknown Course");
        String dateTime = exam.getDate() + " at " + exam.getTime() +
                (exam.getRoom() != null ? " (Room: " + exam.getRoom() + ")" : "");
        textViewExamDate.setText(dateTime);

        String gradeText = exam.getGrade() != null ? "Grade: " + exam.getGrade() : "Grade: Not graded";
        textViewExamGrade.setText(gradeText);

        return convertView;
    }
}
