package interface_adapters.create_assignment;

import interface_adapters.teacher_course.TeacherCourseViewModel;

public class CreateAssignmentPresenter {
    private final TeacherCourseViewModel viewModel;

    public CreateAssignmentPresenter(TeacherCourseViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void refreshView(){
        this.viewModel.firePropertyChanged();
    }
}
