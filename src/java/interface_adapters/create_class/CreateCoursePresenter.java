package interface_adapters.create_class;

import interface_adapters.teacher.TeacherClassesState;
import interface_adapters.teacher.TeacherClassesViewModel;
import use_cases.UserOutputData;

public class CreateCoursePresenter {
    private final TeacherClassesViewModel viewModel;

    public CreateCoursePresenter(TeacherClassesViewModel viewModel) {
        this.viewModel = viewModel;
    }
    public void prepareSuccessView(UserOutputData user) {
        TeacherClassesState state = viewModel.getState();
        state.setUser(user);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
