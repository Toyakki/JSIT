package entities;

import java.util.ArrayList;

public class Course {
    private String instructor;
    private String className;
    private String classCode;
    private ArrayList<String> students;

    public Course(String instructor, String className, String classCode, ArrayList<String> students) {
        this.instructor = instructor;
        this.className = className;
        this.classCode = classCode;
        this.students = students;
    }
}
