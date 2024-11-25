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

    public List<Assignment> getAssignments() {
        return this.assignments;
    }

    public String getName(){
        return this.className;
    }

    public String getClassName() {
        return className;
    }
}
