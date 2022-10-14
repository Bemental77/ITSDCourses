package edu.cvtc.cbement.itsdcourses;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import edu.cvtc.cbement.itsdcourses.ITSDCoursesDatabaseContract.CourseInfoEntry;

public class ITSDCoursesDataWorker {
    // Member Attributes
    private SQLiteDatabase mDb;
    // Constructor
    public ITSDCoursesDataWorker(SQLiteDatabase db) {
        mDb = db;
    }
    private void insertCourse(String title, String description) {
        ContentValues values = new ContentValues();
        values.put(CourseInfoEntry.COLUMN_COURSE_TITLE, title);
        values.put(CourseInfoEntry.COLUMN_COURSE_DESCRIPTION, description);
        long newRowId = mDb.insert(CourseInfoEntry.TABLE_NAME, null, values);
    }
    public void insertCourses() {
        insertCourse("Intro to Computers & Programming",
                "Introductory Computer Course");
        insertCourse("Web 1 - HTML & CSS",
                "Introductory HTML course");
        insertCourse("Programming Fundamentals",
                "Introductory Programming Course");
        insertCourse("Database 1",
                "Introductory Database Course");
        insertCourse("Object Oriented Programming",
                "Second Semester Programming Course using Java");
        insertCourse(".NET Application Development",
                "Second Semester Programming Course using .NET");
        insertCourse("Database 2",
                "Intermediate Database Course");
        insertCourse("Android Development",
                "Application Development Course with Android");
    }
}