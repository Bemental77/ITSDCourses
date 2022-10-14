package edu.cvtc.cbement.itsdcourses;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import edu.cvtc.cbement.itsdcourses.ITSDCoursesDatabaseContract.CourseInfoEntry;
public class DataManager {
    private static DataManager ourInstance = null;
    private List<CourseInfo> mCourses = new ArrayList<>();
    public static DataManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataManager();
        }
        return ourInstance;
    }
    // Return a list of your courses
    public List<CourseInfo> getCourses() {
        return mCourses;
    }
    private static void loadCoursesFromDatabase(Cursor cursor) {
        // Retrieve the field positions in your database.
        // The positions of fields may change over time as the database grows, so
        // you want to use your constants to reference where those positions are in
        // the table.
        int listTitlePosition =
                cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);
        int listDescriptionPosition =
                cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_DESCRIPTION);
        int idPosition = cursor.getColumnIndex(CourseInfoEntry._ID);
        // Create an instance of your DataManager and use the DataManager
        // to clear any information from the array list.
        DataManager dm = getInstance();
        dm.mCourses.clear();
        // Loop through the cursor rows and add new course objects to
        // your array list.
        while (cursor.moveToNext()) {
            String listTitle = cursor.getString(listTitlePosition);
            String listDescription = cursor.getString(listDescriptionPosition);
            int id = cursor.getInt(idPosition);
            CourseInfo list = new CourseInfo(id, listTitle, listDescription);
            dm.mCourses.add(list);
        }
        // Close the cursor (to prevent memory leaks).
        cursor.close();
    }
    public static void loadFromDatabase(ITSDCoursesOpenHelper dbHelper) {
        // Open your database in read mode.
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Create a list of columns you want to return.
        String[] courseColumns = {
                CourseInfoEntry.COLUMN_COURSE_TITLE,
                CourseInfoEntry.COLUMN_COURSE_DESCRIPTION,
                CourseInfoEntry._ID};
        // Create an order by field for sorting purposes.
        String courseOrderBy = CourseInfoEntry.COLUMN_COURSE_TITLE;
        // Populate your cursor with the results of the query.
        final Cursor courseCursor = db.query(CourseInfoEntry.TABLE_NAME,
                courseColumns,
                null, null, null, null,
                courseOrderBy);
        // Call the method to load your array list.
        loadCoursesFromDatabase(courseCursor);
    }
    public int createNewCourse() {
        // Create an empty course object to use on your activity screen
        // when you want a "blank" record to show up. It will return the
        // size of the new course array list.
        CourseInfo course = new CourseInfo(null, null);
        mCourses.add(course);
        return mCourses.size();
    }
    public void removeCourse(int index) {
        mCourses.remove(index);
    }
}