package interface_adapters.teacher;

import use_cases.teacher_course_selection.TeacherCourseViewInteractor;

public class TeacherCourseViewController {
    private final TeacherCourseViewInteractor teacherCourseViewInteractor;

    public TeacherCourseViewController(TeacherCourseViewInteractor teacherCourseViewInteractor) {
        this.teacherCourseViewInteractor = teacherCourseViewInteractor;
    }

    public void viewCourse(String email, String courseName) {
        this.teacherCourseViewInteractor.viewCourse(email, courseName);
    }
}
