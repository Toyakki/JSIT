package interface_adapters.teacher;

import interface_adapters.ViewManagerModel;
import use_cases.teacher_course_back.TeacherCourseBackOutputBoundary;
import use_cases.UserOutputData;

public class TeacherCoursesPresenter implements TeacherCourseBackOutputBoundary {
    private ViewManagerModel viewManagerModel;
    private TeacherClassesViewModel teacherClassesViewModel;

    public TeacherCoursesPresenter(ViewManagerModel viewManagerModel, TeacherClassesViewModel teacherClassesViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.teacherClassesViewModel = teacherClassesViewModel;
    }

    @Override
    public void prepareTeacherCoursesView(UserOutputData userOutputData) {
        final TeacherClassesState teacherClassesState = teacherClassesViewModel.getState();
        teacherClassesState.setUser(userOutputData);
        this.teacherClassesViewModel.setState(teacherClassesState);
        this.teacherClassesViewModel.firePropertyChanged();
        this.viewManagerModel.setState(teacherClassesViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }
}
