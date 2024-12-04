package interface_adapters.join_class;

import interface_adapters.student.StudentClassesState;
import interface_adapters.student.StudentClassesViewModel;
import use_cases.UserOutputData;
import use_cases.join_course.JoinUseCaseOutputBoundary;
import view.StudentClassesView;

public class JoinCoursePresenter implements JoinUseCaseOutputBoundary {
    private final StudentClassesViewModel viewModel;

    public JoinCoursePresenter(StudentClassesViewModel studentClassesViewModel) {
        this.viewModel = studentClassesViewModel;
    }
    
    public void prepareFailView(String error) {
        this.viewModel.getState().setError(error);
        this.viewModel.firePropertyChanged();
    }

    public void prepareSuccessView(UserOutputData user) {
        StudentClassesState state = viewModel.getState();
        state.setUser(user);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
