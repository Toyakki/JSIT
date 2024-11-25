package interface_adapters.join_class;

import view.StudentClassesView;

public class JoinCoursePresenter {
    private StudentClassesView studentClassesView;
    public JoinCoursePresenter(StudentClassesView studentClassesView) {
        this.studentClassesView = studentClassesView;
    }
    public void prepareFailView(String courseDoesNotExist) {
    }

    public void prepareSuccessView(String s) {


    }
}
