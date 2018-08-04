package edu.weber.behunin.justin.cs3270fp;

import java.util.ArrayList;

public class Semester {

    private String sName;
    private ArrayList<Course> courses;

    public Semester() {
        //required empty defualt constructor
    }

    public Semester(String sName) {
        this.sName = sName;
    }

    public Semester(String sName, ArrayList<Course> courses) {
        this.sName = sName;
        this.courses = courses;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public void addClass(Course course){
        courses.add(course);
    }
}
