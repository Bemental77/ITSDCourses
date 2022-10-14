package edu.cvtc.cbement.itsdcourses;

import android.os.Parcel;
import android.os.Parcelable;
public class CourseInfo implements Parcelable {
    // Member Attributes
    private String mTitle;
    private String mDescription;
    private int mId;
    // Overloaded Constructors
    public CourseInfo(String title, String description) {
        mTitle = title;
        mDescription = description;
    }
    public CourseInfo(int id, String title, String description) {
        mId = id;
        mTitle = title;
        mDescription = description;
    }
    // Getters and Setters
    public int getId() {
        return mId;
    }
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }
    public String getDescription() {
        return mDescription;
    }
    public void setDescription(String description) {
        mDescription = description;
    }
    private String getCompareKey() {
        return mTitle + "|" + mDescription;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseInfo that = (CourseInfo) o;
        return getCompareKey().equals(that.getCompareKey());
    }
    @Override
    public int hashCode() {
        return getCompareKey().hashCode();
    }
    @Override
    public String toString() {
        return getCompareKey();
    }
    protected CourseInfo(Parcel parcel) {
        mTitle = parcel.readString();
        mDescription = parcel.readString();
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mTitle);
        parcel.writeString(mDescription);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<CourseInfo> CREATOR = new Creator<CourseInfo>() {
        @Override
        public CourseInfo createFromParcel(Parcel parcel) {
            return new CourseInfo(parcel);
        }
        @Override
        public CourseInfo[] newArray(int size) {
            return new CourseInfo[size];
        }
    };
}
