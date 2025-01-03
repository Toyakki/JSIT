package interface_adapters.student;

import interface_adapters.ViewManagerModel;
import use_cases.student_course_back.StudentCourseBackOutputBoundary;
import use_cases.UserOutputData;

public class StudentCoursesPresenter implements StudentCourseBackOutputBoundary {
    private ViewManagerModel viewManagerModel;
    private StudentClassesViewModel studentClassesViewModel;

    public StudentCoursesPresenter(ViewManagerModel viewManagerModel, StudentClassesViewModel studentClassesViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.studentClassesViewModel = studentClassesViewModel;
    }

    @Override
    public void prepareStudentCoursesView(UserOutputData userOutputData) {
        final StudentClassesState studentClassesState = studentClassesViewModel.getState();
        studentClassesState.setUser(userOutputData);
        this.studentClassesViewModel.setState(studentClassesState);
        this.studentClassesViewModel.firePropertyChanged();
        this.viewManagerModel.setState(studentClassesViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }
}
