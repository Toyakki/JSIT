package interface_adapters.grade;

import interface_adapters.ViewModel;
import interface_adapters.teacher_course.TeacherCourseState;
import interface_adapters.teacher_course.TeacherCourseViewModel;
import use_cases.grade.GradeOutputBoundary;

public class GradePresenter implements GradeOutputBoundary {
    TeacherCourseViewModel viewModel;

    public GradePresenter(TeacherCourseViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void refreshView(TeacherCourseState state) {
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String message) {
        this.viewModel.getState().setError(message);
        viewModel.firePropertyChanged();
    }
}
