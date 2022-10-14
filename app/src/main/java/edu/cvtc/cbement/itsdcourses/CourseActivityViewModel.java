package edu.cvtc.cbement.itsdcourses;

import android.os.Bundle;
import androidx.lifecycle.ViewModel;

public class CourseActivityViewModel extends ViewModel {
    public static final String ORIGINAL_COURSE_TITLE =
            "edu.cvtc.ewackwitz.itsdcourses.ORIGINAL_COURSE_TITLE";
    public static final String ORIGINAL_COURSE_DESCRIPTION =
            "edu.cvtc.ewackwitz.itsdcourses.ORIGINAL_COURSE_DESCRIPTION";
    public String mOriginalCourseTitle;
    public String mOriginalCourseDescription;
    public boolean mIsNewlyCreated = true;
    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_COURSE_TITLE,
                mOriginalCourseTitle);
        outState.putString(ORIGINAL_COURSE_DESCRIPTION,
                mOriginalCourseDescription);
    }
    public void restoreState(Bundle inState) {
        mOriginalCourseTitle =
                inState.getString(ORIGINAL_COURSE_TITLE);
        mOriginalCourseDescription =
                inState.getString(ORIGINAL_COURSE_DESCRIPTION);
    }
}