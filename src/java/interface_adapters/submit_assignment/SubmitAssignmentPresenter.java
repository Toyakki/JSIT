package interface_adapters.submit_assignment;

import interface_adapters.login.LoginState;
import interface_adapters.student_course.StudentCourseState;
import interface_adapters.student_course.StudentCourseViewModel;
import use_cases.submit_assignment.SubmitAssignmentOutputBoundary;

public class SubmitAssignmentPresenter implements SubmitAssignmentOutputBoundary{
   private final StudentCourseViewModel viewModel;

    public SubmitAssignmentPresenter(StudentCourseViewModel viewModel) {
        this.viewModel = viewModel;
    }

   @Override
   public void presentSuccess(String courseName, String email) {

       StudentCourseState studentCourseState = new StudentCourseState(
               courseName,
               email,
               null,
               null,
               null,
               null,
               null
       );
       this.viewModel.setState(studentCourseState);
       this.viewModel.firePropertyChanged();
   }

   @Override
   public void presentError(String message) {
       this.viewModel.setError(message);
       this.viewModel.firePropertyChanged();
   }

}
