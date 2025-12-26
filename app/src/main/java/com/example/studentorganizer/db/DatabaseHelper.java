package com.example.studentorganizer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "student_organizer.db";
    private static final int DATABASE_VERSION = 1;

    // Tabelele si coloanele
    public static final String COURSES_TABLE = "courses";
    public static final String ASSIGNMENTS_TABLE = "assignments";
    public static final String EXAMS_TABLE = "exams";

    public static final String COL_COURSE_ID = "id";
    public static final String COL_COURSE_NAME = "name";
    public static final String COL_COURSE_TEACHER = "teacher";
    public static final String COL_COURSE_ROOM = "room";
    public static final String COL_COURSE_DAY = "day_of_week";
    public static final String COL_COURSE_START_TIME = "start_time";
    public static final String COL_COURSE_END_TIME = "end_time";

    // Coloane assignments
    public static final String COL_ASSIGNMENT_ID = "id";
    public static final String COL_ASSIGNMENT_COURSE_ID = "course_id";
    public static final String COL_ASSIGNMENT_TITLE = "title";
    public static final String COL_ASSIGNMENT_DESCRIPTION = "description";
    public static final String COL_ASSIGNMENT_DEADLINE = "deadline";
    public static final String COL_ASSIGNMENT_STATUS = "status";

    // Coloane exams
    public static final String COL_EXAM_ID = "id";
    public static final String COL_EXAM_COURSE_ID = "course_id";
    public static final String COL_EXAM_DATE = "date";
    public static final String COL_EXAM_TIME = "time";
    public static final String COL_EXAM_ROOM = "room";
    public static final String COL_EXAM_GRADE = "grade";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creează tabel courses
        String CREATE_COURSES_TABLE = "CREATE TABLE " + COURSES_TABLE + " (" +
                COL_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_COURSE_NAME + " TEXT NOT NULL, " +
                COL_COURSE_TEACHER + " TEXT, " +
                COL_COURSE_ROOM + " TEXT, " +
                COL_COURSE_DAY + " INTEGER, " +
                COL_COURSE_START_TIME + " TEXT, " +
                COL_COURSE_END_TIME + " TEXT)";
        db.execSQL(CREATE_COURSES_TABLE);

        // Creează tabel assignments
        String CREATE_ASSIGNMENTS_TABLE = "CREATE TABLE " + ASSIGNMENTS_TABLE + " (" +
                COL_ASSIGNMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ASSIGNMENT_COURSE_ID + " INTEGER NOT NULL, " +
                COL_ASSIGNMENT_TITLE + " TEXT NOT NULL, " +
                COL_ASSIGNMENT_DESCRIPTION + " TEXT, " +
                COL_ASSIGNMENT_DEADLINE + " TEXT, " +
                COL_ASSIGNMENT_STATUS + " TEXT DEFAULT 'TODO', " +
                "FOREIGN KEY(" + COL_ASSIGNMENT_COURSE_ID + ") REFERENCES " +
                COURSES_TABLE + "(" + COL_COURSE_ID + ") ON DELETE CASCADE)";
        db.execSQL(CREATE_ASSIGNMENTS_TABLE);

        // Creează tabel exams
        String CREATE_EXAMS_TABLE = "CREATE TABLE " + EXAMS_TABLE + " (" +
                COL_EXAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EXAM_COURSE_ID + " INTEGER NOT NULL, " +
                COL_EXAM_DATE + " TEXT, " +
                COL_EXAM_TIME + " TEXT, " +
                COL_EXAM_ROOM + " TEXT, " +
                COL_EXAM_GRADE + " REAL, " +
                "FOREIGN KEY(" + COL_EXAM_COURSE_ID + ") REFERENCES " +
                COURSES_TABLE + "(" + COL_COURSE_ID + ") ON DELETE CASCADE)";
        db.execSQL(CREATE_EXAMS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Pentru prima versiune (v1), nu avem upgrade logic
        db.execSQL("DROP TABLE IF EXISTS " + EXAMS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ASSIGNMENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COURSES_TABLE);
        onCreate(db);
    }

}
