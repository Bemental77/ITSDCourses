package edu.cvtc.cbement.itsdcourses;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.cvtc.cbement.itsdcourses.ITSDCoursesDatabaseContract.CourseInfoEntry;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder>{ // Member variables
        public final Context mContext;
        public final LayoutInflater mLayoutInflater;
        // private List<CourseInfo> mCourses;
        private Cursor mCursor;
        private int mCourseTitlePosition;
        private int mCourseDescriptionPosition;
        private int mIdPosition;

        public CourseRecyclerAdapter(Context context, Cursor cursor) {
            mContext = context;
            mCursor = cursor;
            mLayoutInflater = LayoutInflater.from(context);
            // Used to get the positions of the columns we
            // are interested in.
            populateColumnPositions();
        }

        private void populateColumnPositions() {
            if (mCursor != null) {
                // Get column indexes from mCursor
                mCourseTitlePosition =
                        mCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);
                mCourseDescriptionPosition =
                        mCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_DESCRIPTION);
                mIdPosition = mCursor.getColumnIndex(CourseInfoEntry._ID);
            }
        }

        public void changeCursor(Cursor cursor) {
            // If the cursor is open, close it
            if (mCursor != null) {
                mCursor.close();
            }

            // Create a new cursor based upon the object
            // that is passed in.
            mCursor = cursor;

            // Get the positions of the columns in
            // the new cursor.
            populateColumnPositions();

            // Tell the activity that the data set
            // has changed.
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView =
                    mLayoutInflater.inflate(R.layout.item_list, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // Move the cursor to the correct row
            mCursor.moveToPosition(position);

            // Get the actual values
            String courseTitle =
                    mCursor.getString(mCourseTitlePosition);
            String courseDescription =
                    mCursor.getString(mCourseDescriptionPosition);
            int id = mCursor.getInt(mIdPosition);

            // Pass the information to the holder
            holder.mCourseTitle.setText(courseTitle);
            holder.mCourseDescription.setText(courseDescription);
            holder.mId = id;
        }

        @Override
        public int getItemCount() {
            // If the cursor is null, return 0. Otherwise
            // return the count of records in it.
            return mCursor == null ? 0 : mCursor.getCount();
        }

public class ViewHolder extends RecyclerView.ViewHolder {
    // Member variables for inner class
    public final TextView mCourseTitle;
    public final TextView mCourseDescription;
    public int mId;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mCourseTitle
                = (TextView) itemView.findViewById(R.id.course_title);
        mCourseDescription
                = (TextView) itemView.findViewById(R.id.course_description);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CourseActivity.class);
                intent.putExtra(CourseActivity.COURSE_ID, mId);
                mContext.startActivity(intent);
            }
        });
    }
}
}
