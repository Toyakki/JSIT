package interface_adapters.student;

import use_cases.student_course_selection.StudentCourseViewInteractor;

public class StudentCourseViewController {
    private final StudentCourseViewInteractor studentCourseViewInteractor;

    public StudentCourseViewController(StudentCourseViewInteractor studentCourseViewInteractor) {
        this.studentCourseViewInteractor = studentCourseViewInteractor;
    }

    public void viewCourse(String courseName){
        this.studentCourseViewInteractor.viewCourse(courseName);
    }
}
