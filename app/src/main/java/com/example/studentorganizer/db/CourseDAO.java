package com.example.studentorganizer.db;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.studentorganizer.models.Course;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public CourseDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // CREATE
    public long addCourse(Course course) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_COURSE_NAME, course.getName());
        values.put(DatabaseHelper.COL_COURSE_TEACHER, course.getTeacher());
        values.put(DatabaseHelper.COL_COURSE_ROOM, course.getRoom());
        values.put(DatabaseHelper.COL_COURSE_DAY, course.getDayOfWeek());
        values.put(DatabaseHelper.COL_COURSE_START_TIME, course.getStartTime());
        values.put(DatabaseHelper.COL_COURSE_END_TIME, course.getEndTime());

        return db.insert(DatabaseHelper.COURSES_TABLE, null, values);
    }

    // READ - Get all courses
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.COURSES_TABLE, null, null, null, null, null, null);

        if(cursor.moveToFirst()) {
            do {
                Course course = cursorToCourse(cursor);
                courses.add(course);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return courses;
    }

    // READ - Get course by ID
    public Course getCourseById(int id) {
        Cursor cursor = db.query(DatabaseHelper.COURSES_TABLE, null,
                DatabaseHelper.COL_COURSE_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        Course course = null;
        if(cursor.moveToFirst()) {
            course = cursorToCourse(cursor);
        }
        cursor.close();
        return course;
    }

    // UPDATE
    public int updateCourse(Course course) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_COURSE_NAME, course.getName());
        values.put(DatabaseHelper.COL_COURSE_TEACHER, course.getTeacher());
        values.put(DatabaseHelper.COL_COURSE_ROOM, course.getRoom());
        values.put(DatabaseHelper.COL_COURSE_DAY, course.getDayOfWeek());
        values.put(DatabaseHelper.COL_COURSE_START_TIME, course.getStartTime());
        values.put(DatabaseHelper.COL_COURSE_END_TIME, course.getEndTime());

        return db.update(DatabaseHelper.COURSES_TABLE, values,
                DatabaseHelper.COL_COURSE_ID + " = ?",
                new String[]{String.valueOf(course.getId())});
    }

    // DELETE
    public int deleteCourse(int courseId) {
        return db.delete(DatabaseHelper.COURSES_TABLE,
                DatabaseHelper.COL_COURSE_ID + " = ?",
                new String[]{String.valueOf(courseId)});
    }

    // Helper method
    private Course cursorToCourse(Cursor cursor) {
        return new Course(
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_COURSE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_COURSE_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_COURSE_TEACHER)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_COURSE_ROOM)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_COURSE_DAY)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_COURSE_START_TIME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_COURSE_END_TIME))
        );
    }

    public void close() {
        dbHelper.close();
    }
}
