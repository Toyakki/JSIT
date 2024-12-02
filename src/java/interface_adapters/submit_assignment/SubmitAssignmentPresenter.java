package interface_adapters.submit_assignment;

import interface_adapters.student_course.StudentCourseViewModel;
import use_cases.submit_assignment.SubmitAssignmentOutputBoundary;

public class SubmitAssignmentPresenter implements SubmitAssignmentOutputBoundary{
   private final StudentCourseViewModel viewModel;

    public SubmitAssignmentPresenter(StudentCourseViewModel viewModel) {
        this.viewModel = viewModel;
    }

   @Override
   public void presentSuccess() {
       viewModel.setSubmitAssignmentMessage("Assignment submitted successfully", true);
   }

   @Override
   public void presentError() {
       viewModel.setSubmitAssignmentMessage("Error submitting assignment", false);
   }

}
