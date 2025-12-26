# Student Organizer (Android, Java, SQLite)

Student Organizer is an Android application that helps university students manage their academic life in one place. The app is intentionally simple, offline-first, and easy to understand, making it suitable both as a real-world utility and as an academic project.

---

## Overview

The app provides:

- Course management (with schedule information)
- Assignment tracking with status and deadlines
- Exam management with optional grades
- Weekly schedule (timetable) view
- Dashboard with GPA and progress statistics
- A clean main menu for navigation

Tech stack:

- Android (Java, AndroidX)
- SQLite via a lightweight DAO layer
- Classic Activities, ListView with custom adapters, and XML layouts

---

## Features

### Courses

- Create, edit, delete courses
- Store:
  - Course name
  - Professor
  - Room
  - Day of week
  - Start and end time
  - Credits
- List all courses in a simple ListView

Used by:
- Assignments (via `courseId`)
- Exams (via `courseId`)
- Schedule (for the weekly timetable)

### Assignments

- Create assignments linked to a course
- Fields:
  - Course
  - Title
  - Description
  - Deadline (date)
  - Status: `TODO`, `IN_PROGRESS`, `DONE`
- Full CRUD:
  - Add, edit, delete
  - Filter by course
- Visual status in the list:
  - Red = TODO
  - Orange = IN_PROGRESS
  - Green = DONE

### Exams

- Create exams linked to a course
- Fields:
  - Course
  - Date
  - Time
  - Room
  - Grade (optional, `Double`, can be `null`)
- Full CRUD:
  - Add, edit, delete
  - Filter by course
- Sorted by exam date
- Displays “Not graded” when grade is missing

### Weekly Schedule (Timetable)

- Weekly view (Monday–Friday)
- Reads all courses from the database
- Groups courses by `dayOfWeek`
- Sorts each day by `startTime`
- For each day, displays:
  - Time interval (start–end)
  - Course name
  - Professor
  - Room
- Shows a friendly “No courses” message for empty days

### Dashboard (Statistics & GPA)

Central hub for academic statistics:

- Overall GPA
  - Computed as the average of all non-null exam grades
  - Color-coded:
    - Green (≥ 8.5) – Excellent
    - Blue (≥ 7.0) – Good
    - Amber (≥ 5.0) – Fair
    - Red (< 5.0) – Poor

- Per-course GPA
  - Average grade for each course based on its exams
  - Displayed as a list of `Course name → GPA`

- General statistics
  - Total number of courses
  - Total number of exams
  - Graded exams vs total exams
  - Total number of assignments
  - Completed assignments vs total

- Assignment status overview
  - Count of TODO / IN_PROGRESS / DONE
  - Overall completion percentage for assignments

---

## Architecture

### Activities

- `MainActivity`
  - Acts as the main menu
  - Buttons:
    - View Courses
    - View Assignments
    - View Exams
    - Dashboard
    - Schedule

- `CourseListActivity` / `CourseFormActivity`
  - List and form for managing courses

- `AssignmentListActivity` / `AssignmentFormActivity`
  - List and form for managing assignments
  - Supports filtering by course when launched with a `course_id` extra

- `ExamListActivity` / `ExamFormActivity`
  - List and form for managing exams
  - Supports filtering by course

- `ScheduleActivity`
  - Builds and displays the weekly timetable dynamically

- `DashboardActivity`
  - Loads data from all DAOs and computes statistics and GPA

### Data Layer

- `DatabaseHelper`
  - Extends `SQLiteOpenHelper`
  - Creates and upgrades the SQLite database
  - Defines tables:
    - `courses`
    - `assignments`
    - `exams`

- `CourseDAO`, `AssignmentDAO`, `ExamDAO`
  - Encapsulate CRUD operations for each entity
  - Convert between `Cursor` objects and model objects

### Models

- `Course`
  - `id`, `name`, `professor`, `room`, `dayOfWeek`, `startTime`, `endTime`, `credits`

- `Assignment`
  - `id`, `courseId`, `title`, `description`, `deadline`, `status`

- `Exam`
  - `id`, `courseId`, `date`, `time`, `room`, `grade` (nullable)

### UI Components

- `res/layout/`
  - XML layouts for all activities:
    - main menu, lists, forms, schedule, dashboard
- `res/values/`
  - `strings.xml`, `colors.xml`, `styles.xml` and optional light/dark theme resources

---

## Project Structure (Simplified)

app/

  src/main/java/com/example/studentorganizer/
  
    MainActivity.java
    activities/
      CourseListActivity.java
      CourseFormActivity.java
      AssignmentListActivity.java
      AssignmentFormActivity.java
      ExamListActivity.java
      ExamFormActivity.java
      ScheduleActivity.java
      DashboardActivity.java
    db/
      DatabaseHelper.java
      CourseDAO.java
      AssignmentDAO.java
      ExamDAO.java
    models/
      Course.java
      Assignment.java
      Exam.java
    adapters/
      CourseAdapter.java
      AssignmentAdapter.java
      ExamAdapter.java
  src/main/res/
    layout/
    values/
AndroidManifest.xml
build.gradle(.kts)
settings.gradle(.kts)

---

## How to Run

1. Clone the repository:
   git clone https://github.com/<username>/student-organizer-android.git

2. Open the project in Android Studio.

3. Let Gradle sync and download dependencies.

4. Connect an Android device or start an emulator.

5. Run the app.

The application starts in `MainActivity`, from where you can navigate to Courses, Assignments, Exams, Dashboard, and Schedule.

---

## Notes

- All data is stored locally using SQLite (no network required).
- The project is intentionally kept framework-light and uses plain Java, Activities, and DAOs to be easy to read, understand, and extend.
