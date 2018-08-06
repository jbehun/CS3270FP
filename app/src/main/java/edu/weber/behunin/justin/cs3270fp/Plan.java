package edu.weber.behunin.justin.cs3270fp;

import java.util.ArrayList;

public class Plan {

    private ArrayList<Semester> semesterList = new ArrayList<>();
    private String planName;

    public Plan() {
        //required empty constructor
    }

    public Plan(String planName) {
        this.planName = planName;
    }

    public Plan(ArrayList<Semester> semesterList, String planName) {
        this.semesterList = semesterList;
        this.planName = planName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public ArrayList<Semester> getSemesterList() {
        return semesterList;
    }

    public void setSemesterList(ArrayList<Semester> semesterList) {
        this.semesterList = semesterList;
    }

    public void addSemester(Semester semester) {
        semesterList.add(semester);
    }

    public Semester getSemester(Semester semester) {
        for (int i = 0; i < semesterList.size(); i++) {
            if (semester.getSemesterName().equals(semesterList.get(i).getSemesterName())) {
                return semesterList.get(i);
            }
        }

        return new Semester();
    }

    public void deleteSemester(Semester semester) {

        for (int i = 0; i < semesterList.size(); i++) {
            if (semester.getSemesterName().equals(semesterList.get(i).getSemesterName())) {
                semesterList.remove(i);
            }
        }
    }

}
