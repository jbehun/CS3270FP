package edu.weber.behunin.justin.cs3270fp;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Prerequisite {

    private String courseID;
    private ArrayList<String> prereqList;

    public Prerequisite() {
        //required empty default class for Firebase
    }

    public Prerequisite(String courseID, ArrayList<String> prereqList) {
        this.courseID = courseID;
        this.prereqList = prereqList;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public ArrayList<String> getPrereqList() {
        return prereqList;
    }

    public void setPrereqList(ArrayList<String> prereqList) {
        this.prereqList = prereqList;
    }

    public void addPrereq(String prereq) {
        prereqList.add(prereq);
    }

    public void clearPrereqList() {
        prereqList.clear();
    }
}
