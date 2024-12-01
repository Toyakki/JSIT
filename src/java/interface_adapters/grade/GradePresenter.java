package interface_adapters.grade;

import interface_adapters.teacher_course.TeacherCourseState;
import interface_adapters.teacher_course.TeacherCourseViewModel;

public class GradePresenter {
    private TeacherCourseViewModel viewModel;

    public GradePresenter(TeacherCourseViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void prepareFailView(String error) {
        viewModel.getState().setError(error);
        viewModel.firePropertyChanged();
    }

    public void refreshView(TeacherCourseState state) {
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
