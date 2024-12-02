package use_cases.grade;

import interface_adapters.teacher_course.TeacherCourseState;

public interface GradeOutputBoundary {
    public void refreshView(TeacherCourseState state);
    public void prepareFailView(String message);
}
