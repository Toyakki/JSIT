package interface_adapters.StudentCourse;

import entities.Course;
import interface_adapters.ViewManagerModel;
import use_cases.UserOutputData;
import view.StudentCourseView;

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
