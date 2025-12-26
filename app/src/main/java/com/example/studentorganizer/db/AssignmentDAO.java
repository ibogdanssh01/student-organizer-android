package com.example.studentorganizer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.studentorganizer.models.Assignment;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public AssignmentDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // CREATE
    public long addAssignment(Assignment assignment) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_ASSIGNMENT_COURSE_ID, assignment.getCourseId());
        values.put(DatabaseHelper.COL_ASSIGNMENT_TITLE, assignment.getTitle());
        values.put(DatabaseHelper.COL_ASSIGNMENT_DESCRIPTION, assignment.getDescription());
        values.put(DatabaseHelper.COL_ASSIGNMENT_DEADLINE, assignment.getDeadline());
        values.put(DatabaseHelper.COL_ASSIGNMENT_STATUS, assignment.getStatus());

        return db.insert(DatabaseHelper.ASSIGNMENTS_TABLE, null, values);
    }

    // READ - Get all assignments
    public List<Assignment> getAllAssignments() {
        List<Assignment> assignments = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.ASSIGNMENTS_TABLE, null, null,
                null, null, null,
                DatabaseHelper.COL_ASSIGNMENT_DEADLINE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Assignment assignment = cursorToAssignment(cursor);
                assignments.add(assignment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return assignments;
    }

    // READ - Get assignment by ID
    public Assignment getAssignmentById(int id) {
        Cursor cursor = db.query(DatabaseHelper.ASSIGNMENTS_TABLE, null,
                DatabaseHelper.COL_ASSIGNMENT_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        Assignment assignment = null;
        if (cursor.moveToFirst()) {
            assignment = cursorToAssignment(cursor);
        }
        cursor.close();
        return assignment;
    }

    // READ - Get assignments by course
    public List<Assignment> getAssignmentsByCourse(int courseId) {
        List<Assignment> assignments = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.ASSIGNMENTS_TABLE, null,
                DatabaseHelper.COL_ASSIGNMENT_COURSE_ID + " = ?",
                new String[]{String.valueOf(courseId)},
                null, null,
                DatabaseHelper.COL_ASSIGNMENT_DEADLINE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Assignment assignment = cursorToAssignment(cursor);
                assignments.add(assignment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return assignments;
    }

    // READ - Get assignments by status
    public List<Assignment> getAssignmentsByStatus(String status) {
        List<Assignment> assignments = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.ASSIGNMENTS_TABLE, null,
                DatabaseHelper.COL_ASSIGNMENT_STATUS + " = ?",
                new String[]{status},
                null, null,
                DatabaseHelper.COL_ASSIGNMENT_DEADLINE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Assignment assignment = cursorToAssignment(cursor);
                assignments.add(assignment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return assignments;
    }

    // UPDATE
    public int updateAssignment(Assignment assignment) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_ASSIGNMENT_COURSE_ID, assignment.getCourseId());
        values.put(DatabaseHelper.COL_ASSIGNMENT_TITLE, assignment.getTitle());
        values.put(DatabaseHelper.COL_ASSIGNMENT_DESCRIPTION, assignment.getDescription());
        values.put(DatabaseHelper.COL_ASSIGNMENT_DEADLINE, assignment.getDeadline());
        values.put(DatabaseHelper.COL_ASSIGNMENT_STATUS, assignment.getStatus());

        return db.update(DatabaseHelper.ASSIGNMENTS_TABLE, values,
                DatabaseHelper.COL_ASSIGNMENT_ID + " = ?",
                new String[]{String.valueOf(assignment.getId())});
    }

    // DELETE
    public int deleteAssignment(int assignmentId) {
        return db.delete(DatabaseHelper.ASSIGNMENTS_TABLE,
                DatabaseHelper.COL_ASSIGNMENT_ID + " = ?",
                new String[]{String.valueOf(assignmentId)});
    }

    // Helper method
    private Assignment cursorToAssignment(Cursor cursor) {
        return new Assignment(
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ASSIGNMENT_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ASSIGNMENT_COURSE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ASSIGNMENT_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ASSIGNMENT_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ASSIGNMENT_DEADLINE)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ASSIGNMENT_STATUS))
        );
    }

    public void close() {
        dbHelper.close();
    }
}
