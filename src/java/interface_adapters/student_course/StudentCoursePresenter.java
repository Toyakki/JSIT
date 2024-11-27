package interface_adapters.student_course;

import interface_adapters.ViewManagerModel;
import use_cases.UserOutputData;

public class StudentCoursePresenter {
    private StudentCourseViewModel viewModel;
    private ViewManagerModel viewManagerModel;

    public StudentCoursePresenter(StudentCourseViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    public void prepareStudentCourseView(UserOutputData userOutputData) {
        //how this will work how is data set
    }

}
