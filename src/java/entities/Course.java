package entities;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String instructor;
    private String className;
    private String classCode;
    private List<String> students;
    private List<Assignment> assignments;

    public Course(String instructor,
                  String className,
                  String classCode,
                  List<String> students,
                  List<Assignment> assignments) {
        this.instructor = instructor;
        this.className = className;
        this.classCode = classCode;
        this.students = students;
        this.assignments = assignments;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public List<Assignment> getAssignments() {
        return List.copyOf(this.assignments);
    }

    public List<String> getStudentEmails() {
        return List.copyOf(this.students);
    }

    public String getName(){
        return this.className;
    }

    public String getClassName() {
        return className;
    }

    public void addAssignment(Assignment assignment) {
        this.assignments.add(assignment);
    }

    public void addStudent(String student) {
        this.students.add(student);
    }

    public String getCourseCode() {
        return classCode;
    }
}
