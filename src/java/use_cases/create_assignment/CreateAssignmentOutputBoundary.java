package use_cases.create_assignment;

import interface_adapters.teacher_course.TeacherCourseState;

public interface CreateAssignmentOutputBoundary {
    void prepareErrorView(String error);
    void refreshView(TeacherCourseState state);
}
