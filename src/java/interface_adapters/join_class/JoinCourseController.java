package interface_adapters.join_class;

import use_cases.join_class.JoinUseCaseInteractor;

public class JoinCourseController {
    private JoinUseCaseInteractor interactor;

    public JoinCourseController(JoinUseCaseInteractor interactor) {
        this.interactor = interactor;
    }

    public void joinCourse(String studentEmail, String courseName) {
        // call the interactor with the relevant data
        this.interactor.joinCourse(studentEmail, courseName);
    }
}
