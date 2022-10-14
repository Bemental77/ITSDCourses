package edu.cvtc.cbement.itsdcourses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import edu.cvtc.cbement.itsdcourses.ITSDCoursesDatabaseContract.CourseInfoEntry;

public class CourseActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    // Constants
    public static final String COURSE_ID =
            "edu.cvtc.ewackwitz.itsdcourses.COURSE_ID";
    public static final String ORIGINAL_COURSE_TITLE =
            "edu.cvtc.ewackwitz.itsdcourses.ORIGINAL_COURSE_TITLE";
    public static final String ORIGINAL_COURSE_DESCRIPTION =
            "edu.cvtc.ewackwitz.itsdcourses.ORIGINAL_COURSE_DESCRIPTION";
    public static final int ID_NOT_SET = -1;
    public static final int LOADER_COURSES = 0;
    // Initialize new CourseInfo to empty
    private CourseInfo mCourse = new CourseInfo(0, "", "");
    // Member variables
    private boolean mIsNewCourse;
    private boolean mIsCancelling;
    private int mCourseId;
    private int mCourseTitlePosition;
    private int mCourseDescriptionPosition;
    private String mOriginalCourseTitle;
    private String mOriginalCourseDescription;
    // Member objects
    private EditText mTextCourseTitle;
    private EditText mTextCourseDescription;
    private ITSDCoursesOpenHelper mDbOpenHelper;
    private Cursor mCourseCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        mDbOpenHelper = new ITSDCoursesOpenHelper(this);
        readDisplayStateValues();
        // If the bundle is null, save the values. Otherwise
        // restore the original values.
        if (savedInstanceState == null) {
            saveOriginalCourseValues();
        } else {
            restoreOriginalCourseValues(savedInstanceState);
        }
        mTextCourseTitle = findViewById(R.id.text_course_title);
        mTextCourseDescription = findViewById(R.id.text_course_description);
        // If it is not a new course, load the course data into the layout
        if (!mIsNewCourse) {
            LoaderManager.getInstance(this).initLoader(LOADER_COURSES, null, this);
        }
    }

    private void displayCourse() {
        // Retrieve the values from the cursor based upon
        // the position of the columns.
        String courseTitle =
                mCourseCursor.getString(mCourseTitlePosition);
        String courseDescription =
                mCourseCursor.getString(mCourseDescriptionPosition);
        // Use the information to populate the layout.
        mTextCourseTitle.setText(courseTitle);
        mTextCourseDescription.setText(courseDescription);
    }

    private void saveOriginalCourseValues() {
        // Only save values if you do not have a new course
        if (!mIsNewCourse) {
            mOriginalCourseTitle = mCourse.getTitle();
            mOriginalCourseDescription = mCourse.getDescription();
        }
    }

    private void restoreOriginalCourseValues(Bundle savedInstanceState) {
        // Get the original saved values from the savedInstanceState
        mOriginalCourseTitle =
                savedInstanceState.getString(ORIGINAL_COURSE_TITLE);
        mOriginalCourseDescription =
                savedInstanceState.getString(ORIGINAL_COURSE_DESCRIPTION);
    }

    private void readDisplayStateValues() {
        // Ge the intent passed into the activity
        Intent intent = getIntent();
        // Get the course id passed into the intent
        mCourseId = intent.getIntExtra(COURSE_ID, ID_NOT_SET);
        // If the course id is not set, create a new course
        mIsNewCourse = mCourseId == ID_NOT_SET;
        if (mIsNewCourse) {
            createNewCourse();
        }
    }

    private void createNewCourse() {
        // Create ContentValues object to hold our fields
        ContentValues values = new ContentValues();
        // For a new course, we don't know what the values
        // will be, so we set the columns to empty strings.
        values.put(CourseInfoEntry.COLUMN_COURSE_TITLE, "");
        values.put(CourseInfoEntry.COLUMN_COURSE_DESCRIPTION, "");
        // Get connection to the database. Use the writeable
        // method since we are changing the data.
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        // Insert the new row in the database and assign the new id
        // to our member variable for course id. Cast the "long"
        // return value to an int.
        mCourseId = (int) db.insert(CourseInfoEntry.TABLE_NAME, null, values);
    }

    private void saveCourseToDatabase(String courseTitle,
                                      String courseDescription) {
        // Create selection criteria
        final String selection = CourseInfoEntry._ID + " = ?";
        final String[] selectionArgs = {Integer.toString(mCourseId)};
        // Use a ContentValues object to put our information into.
        final ContentValues values = new ContentValues();
        values.put(CourseInfoEntry.COLUMN_COURSE_TITLE, courseTitle);
        values.put(CourseInfoEntry.COLUMN_COURSE_DESCRIPTION, courseDescription);

        AsyncTaskLoader<String> task = new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {
                // Get connection to the database. Use the writeable
                // method since we are changing the data.
                SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
                // Call the update method
                db.update(CourseInfoEntry.TABLE_NAME, values, selection, selectionArgs);
                return null;
            }
        };
        task.loadInBackground();
    }

    private void storePreviousCourseValues() {
        mCourse.setTitle(mOriginalCourseTitle);
        mCourse.setDescription(mOriginalCourseDescription);
    }

    private void saveCourse() {
        // Get the values from the layout
        String courseTitle = mTextCourseTitle.getText().toString();
        String courseDescription = mTextCourseDescription.getText().toString();
        // Call the method to write to the database
        saveCourseToDatabase(courseTitle, courseDescription);
    }

    private void deleteCourseFromDatabase() {
        // Create selection criteria
        final String selection = CourseInfoEntry._ID + " = ?";
        final String[] selectionArgs = {Integer.toString(mCourseId)};
        AsyncTaskLoader<String> task = new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {
                // Get connection to the database. Use the writable
                // method since we are changing the data.
                SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
                // Call the delete method
                db.delete(CourseInfoEntry.TABLE_NAME, selection, selectionArgs);
                return null;
            }
        };
        task.loadInBackground();
    }

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    // Called to create the loader
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        // Create a local cursor loader
        CursorLoader loader = null;
        // Check to see if the id is for your loader
        if (id == LOADER_COURSES) {
            loader = createLoaderCourses();
        }

        return loader;
    }

    private CursorLoader createLoaderCourses() {
        return new CursorLoader(this) {
            @Override
            public Cursor loadInBackground() {
                // Open a connection to the database
                SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
                // Build the selection criteria. In this case, you want
                // to set the ID of the course to the passed-in
                // course id from the Intent.
                String selection = CourseInfoEntry._ID + " = ?";
                String[] selectionArgs = {Integer.toString(mCourseId)};
                // Create a list of the columns you are pulling from
                // the database
                String[] courseColumns = {
                        CourseInfoEntry.COLUMN_COURSE_TITLE,
                        CourseInfoEntry.COLUMN_COURSE_DESCRIPTION
                };
                // Fill your cursor with the information you have provided.
                return db.query(CourseInfoEntry.TABLE_NAME, courseColumns,
                        selection, selectionArgs, null, null, null);
            }
        };
    }

    @Override
    // Called when data is loaded
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // Check to see if this is your cursor for your loader
        if (loader.getId() == LOADER_COURSES) {
            loadFinishedCourses(data);
        }
    }

    private void loadFinishedCourses(Cursor data) {
        // Populate your member cursor with the data.
        mCourseCursor = data;
        // Get the positions of the fields in the cursor so that
        // you are able to retrieve them into your layout
        mCourseTitlePosition =
                mCourseCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);
        mCourseDescriptionPosition =
                mCourseCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_DESCRIPTION);
        // Make sure that you have moved to the correct record.
        // The cursor will not have populated any of the
        // fields until you move it.
        mCourseCursor.moveToNext();
        // Call the method to display the course.
        displayCourse();
    }

    @Override
    // Called during cleanup
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // Check to see if this is your cursor for your loader.
        if (loader.getId() == LOADER_COURSES) {
            // If the cursor is not null, close it
            if (mCourseCursor != null) {
                mCourseCursor.close();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu - this adds items to the action bar
        // if it is present.
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();
        if (id == R.id.action_cancel) {
            mIsCancelling = true;
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Did the user cancel the process?
        if (mIsCancelling) {
            // Is this a new Course?
            if (mIsNewCourse) {
                // Delete the new course
                deleteCourseFromDatabase();
            } else {
                // Put the original values on the screen.
                storePreviousCourseValues();
            }
        } else {
            // Save the data when leaving the activity
            saveCourse();
        }
    }
}

