package edu.weber.behunin.justin.cs3270fp;

import java.util.ArrayList;

public class Semester {

    private String semesterName;
    private ArrayList<Course> courses = new ArrayList<>();

    public Semester() {
        //required empty defualt constructor
    }

    public Semester(String semesterName) {
        this.semesterName = semesterName;
    }

    public Semester(String semesterName, ArrayList<Course> courses) {
        this.semesterName = semesterName;
        this.courses = courses;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void deleteCourse(Course course) {
        for (int i = 0; i < courses.size(); i++) {
            if (course.getCourseID().equals(courses.get(i).getCourseID())) {
                courses.remove(i);
            }
        }
    }
}
