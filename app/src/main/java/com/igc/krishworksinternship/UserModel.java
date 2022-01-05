package com.igc.krishworksinternship;

public class UserModel {
    private String studentId;
    private String studentName;
    private String marks;
    private String result;


    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentId;
    }

    public String getmarks() {
        return marks;
    }

    public void setmarks(String marks) {
        this.marks = marks;
    }

    public String getResult(){ return result; }

    public void  setResult(String result){ this.result = result; }

    public UserModel(String studentId,String studentName,String marks)
    {
        this.studentId = studentId;
        this.studentName = studentName;
        this.marks = marks;
    }

    public UserModel(String studentId,String studentName,String marks,String result)
    {
        this.studentId = studentId;
        this.studentName = studentName;
        this.marks = marks;
        this.result = result;
    }
}
