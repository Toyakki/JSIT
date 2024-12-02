package interface_adapters.submit_assignment;

import entities.Assignment;
import interface_adapters.login.LoginState;
import interface_adapters.student_course.StudentCourseState;
import interface_adapters.student_course.StudentCourseViewModel;
import use_cases.submit_assignment.SubmitAssignmentOutputBoundary;

import java.util.ArrayList;
import java.util.List;

public class SubmitAssignmentPresenter implements SubmitAssignmentOutputBoundary{
   private final StudentCourseViewModel viewModel;

    public SubmitAssignmentPresenter(StudentCourseViewModel viewModel) {
        this.viewModel = viewModel;
    }

   @Override
   public void presentSuccess(String courseName, String email, Assignment assignment) {


       List<String> assignmentNames = new ArrayList<>();
       List<String> dueDates = new ArrayList<>();
       List<String> marks = new ArrayList<>();
       List<String> stages  = new ArrayList<>();
       List<String> marksRecievedStatus = new ArrayList<>();

       assignmentNames.add(assignment.getName());
       dueDates.add(assignment.getDueDate());
       marks.add(assignment.getMarks());
       stages.add(assignment.getStage());
       marksRecievedStatus.add(assignment.getMarks());

       StudentCourseState studentCourseState = new StudentCourseState(
               courseName,
               email,
               assignmentNames,
               dueDates,
               marks,
               stages,
               marksRecievedStatus
       );
       this.viewModel.setState(studentCourseState);
       this.viewModel.firePropertyChanged();
   }

   @Override
   public void presentError(String message) {
//       this.viewModel.setError(message);
       this.viewModel.firePropertyChanged();
   }

}
