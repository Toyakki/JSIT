package interface_adapters.create_assignment;

import interface_adapters.teacher_course.TeacherCourseState;
import interface_adapters.teacher_course.TeacherCourseViewModel;
import use_cases.create_assignment.CreateAssignmentOutputBoundary;

public class CreateAssignmentPresenter implements CreateAssignmentOutputBoundary {
    private final TeacherCourseViewModel viewModel;

    public CreateAssignmentPresenter(TeacherCourseViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void prepareErrorView(String errorMessage) {
        this.viewModel.getState().setError(errorMessage);
        this.viewModel.firePropertyChanged();
    }

    public void refreshView(TeacherCourseState state) {
        this.viewModel.setState(state);
        this.viewModel.firePropertyChanged();
    }
}
