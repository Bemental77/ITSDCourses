package edu.cvtc.cbement.itsdcourses;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.cvtc.cbement.itsdcourses.databinding.ActivityMainBinding;
import edu.cvtc.cbement.itsdcourses.ITSDCoursesDatabaseContract.CourseInfoEntry;
import android.view.Menu;
import android.view.MenuItem;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private ActivityMainBinding binding;
    // Constants
    public static final int LOADER_COURSES = 0;
    // Member variables
    private ITSDCoursesOpenHelper mDbOpenHelper;
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mCoursesLayoutManager;
    private CourseRecyclerAdapter mCourseRecyclerAdapter;
    private boolean mIsCreated = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        mDbOpenHelper = new ITSDCoursesOpenHelper(this);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CourseActivity.class));
            }
        });
        initializeDisplayContent();
    }
    private void initializeDisplayContent() {
        // Retrieve the information from your database
        DataManager.loadFromDatabase(mDbOpenHelper);
        // Set a reference to your list of items layout
        mRecyclerItems = (RecyclerView) findViewById(R.id.list_items);
        mCoursesLayoutManager = new LinearLayoutManager(this);
        // Get your courses
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        // We do not have a cursor yet, so pass null.
        mCourseRecyclerAdapter = new CourseRecyclerAdapter(this, null);
        // Display the courses
        displayCourses();
    }
    private void displayCourses() {
        mRecyclerItems.setLayoutManager(mCoursesLayoutManager);
        mRecyclerItems.setAdapter(mCourseRecyclerAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Use restartLoader instead of initLoader to make sure
        // you re-query the database each time the activity is
        // loaded in the app.
        LoaderManager.getInstance(this).restartLoader(LOADER_COURSES, null, this);
    }
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        // Create new cursor loader
        CursorLoader loader = null;
        if (id == LOADER_COURSES) {
            loader = new CursorLoader(this) {
                @Override
                public Cursor loadInBackground() {
                    mIsCreated = true;
                    // Open your database in read mode.
                    SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
                    // Create a list of columns you want to return.
                    String[] courseColumns = {
                            CourseInfoEntry.COLUMN_COURSE_TITLE,
                            CourseInfoEntry.COLUMN_COURSE_DESCRIPTION,
                            CourseInfoEntry._ID
                    };
                    // Create an order by field for sorting purposes.
                    String courseOrderBy = CourseInfoEntry.COLUMN_COURSE_TITLE;
                    // Populate your cursor with the results of the query.
                    return db.query(CourseInfoEntry.TABLE_NAME,
                            courseColumns, null, null, null, null,
                            courseOrderBy);
                }
            };
        }
        return loader;
    }
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOADER_COURSES && mIsCreated) {
            // Associate the cursor with your RecyclerAdapter
            mCourseRecyclerAdapter.changeCursor(data);
            mIsCreated = false;
        }
    }
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (loader.getId() == LOADER_COURSES) {
            // Change the cursor to null (cleanup)
            mCourseRecyclerAdapter.changeCursor(null);
        }
    }
}