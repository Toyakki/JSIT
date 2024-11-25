package use_cases.student_course_selection;

import interface_adapters.student.StudentCourseViewPresenter;

public class StudentCourseViewInteractor {
    private StudentCourseViewPresenter presenter;

    // eventually replace all presenters and managers with proper interfaces for flexability
    public StudentCourseViewInteractor(StudentCourseViewPresenter presenter) {
        this.presenter = presenter;
    }

    public void viewCourse(String courseName){
        // temporarily just pass through the course name
        // logic to pull all the assignments and relevant information will go here later...
        this.presenter.prepareView(courseName);
    }
}
