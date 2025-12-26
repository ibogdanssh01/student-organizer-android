package com.example.studentorganizer.db;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.studentorganizer.models.Exam;
import java.util.ArrayList;
import java.util.List;

public class ExamDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public ExamDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // CREATE
    public long addExam(Exam exam) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_EXAM_COURSE_ID, exam.getCourseId());
        values.put(DatabaseHelper.COL_EXAM_DATE, exam.getDate());
        values.put(DatabaseHelper.COL_EXAM_TIME, exam.getTime());
        values.put(DatabaseHelper.COL_EXAM_ROOM, exam.getRoom());

        if (exam.getGrade() != null ) {
            values.put(DatabaseHelper.COL_EXAM_GRADE, exam.getGrade());
        }

        return db.insert(DatabaseHelper.EXAMS_TABLE, null, values);
    }

   public List<Exam> getAllExams() {
        List<Exam> exams = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.EXAMS_TABLE, null,
                null, null, null, null,
                DatabaseHelper.COL_EXAM_DATE + " ASC");
        if (cursor.moveToFirst()) {
            do {
                Exam exam = cursorToExam(cursor);
                exams.add(exam);
            } while(cursor.moveToNext());
        }

        cursor.close();
        return exams;
   }


   // READ - get exam by id
    public Exam getExamById(int id) {
        Cursor cursor = db.query(DatabaseHelper.EXAMS_TABLE,
                null,
                DatabaseHelper.COL_EXAM_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);
        Exam exam = null;
        if (cursor.moveToFirst()) {
            exam = cursorToExam(cursor);
        }

        cursor.close();
        return exam;
    }


    public List<Exam> getExamByCourse(int courseId) {
        List<Exam> exams = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.EXAMS_TABLE, null,
                DatabaseHelper.COL_EXAM_ID + " = ?",
                new String[]{String.valueOf(courseId)},
                null, null,
                DatabaseHelper.COL_EXAM_DATE + " ASC");

        if(cursor.moveToFirst()) {
            do {
                Exam exam = cursorToExam(cursor);
                exams.add(exam);
            }while(cursor.moveToNext());
        }

        cursor.close();
        return exams;
    }

    // UPDATE
    public int updateExam(Exam exam) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_EXAM_COURSE_ID, exam.getCourseId());
        values.put(DatabaseHelper.COL_EXAM_ROOM, exam.getRoom());
        values.put(DatabaseHelper.COL_EXAM_DATE, exam.getDate());
        values.put(DatabaseHelper.COL_EXAM_TIME, exam.getTime());

        if(exam.getGrade() != null) {
            values.put(DatabaseHelper.COL_EXAM_GRADE, exam.getGrade());
        } else {
            values.putNull(DatabaseHelper.COL_EXAM_GRADE);
        }

        return db.update(DatabaseHelper.EXAMS_TABLE, values,
                DatabaseHelper.COL_EXAM_ID + " = ?",
                new String[]{String.valueOf(exam.getId())});
    }
    // DELETE
    public int deleteExam(int examId) {
        return db.delete(
                DatabaseHelper.EXAMS_TABLE,
                DatabaseHelper.COL_EXAM_ID + " = ?",
                new String[]{String.valueOf(examId)}
        );
    }


    // Helper method
    private Exam cursorToExam(Cursor cursor) {
        int gradeIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EXAM_GRADE);
        Double grade = null;
        if(!cursor.isNull(gradeIndex)) {
            grade = cursor.getDouble(gradeIndex);
        }

        return new Exam(
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EXAM_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EXAM_COURSE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EXAM_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EXAM_TIME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EXAM_ROOM)),
                grade
        );
    }

    public void close() {
        dbHelper.close();
    }
}
