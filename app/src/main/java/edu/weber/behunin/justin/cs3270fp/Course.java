package edu.weber.behunin.justin.cs3270fp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Course {

    private String courseID;
    private String courseName;
    private boolean prereqRequired;

    public Course() {
        //required default constructor for Firebase
    }

    public Course(String courseID, String courseName, boolean prereqRequired) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.prereqRequired = prereqRequired;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public boolean isPrereqRequired() {
        return prereqRequired;
    }

    public void setPrereqRequired(boolean prereqRequired) {
        this.prereqRequired = prereqRequired;
    }

    @Override
    public String toString() {
        return courseID;
    }
}
