package edu.cvtc.cbement.itsdcourses;

import android.provider.BaseColumns;

public final class ITSDCoursesDatabaseContract {

    private ITSDCoursesDatabaseContract() {}

    public static final class CourseInfoEntry implements BaseColumns {
        public static final String
                TABLE_NAME = "course_info";
        public static final String
                COLUMN_COURSE_TITLE = "course_title";
        public static final String
                COLUMN_COURSE_DESCRIPTION = "course_description";
        public static final String INDEX1 = TABLE_NAME + "_index1";
        public static final String SQL_CREATE_INDEX1 =
                "CREATE INDEX " + INDEX1 + " ON " + TABLE_NAME +
                        "(" + COLUMN_COURSE_TITLE + ")";
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_COURSE_TITLE + " TEXT NOT NULL, " +
                        COLUMN_COURSE_DESCRIPTION + " TEXT)";
    }
}