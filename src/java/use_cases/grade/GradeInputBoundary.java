package use_cases.grade;

import java.io.File;

public interface GradeInputBoundary {
    public void gradeAssignment(String grade, File file, String studentEmail, String teacherEmail,
                                String courseName, String assignmentName);
}
