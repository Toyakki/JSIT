package interface_adapters.submit_assignment;

import use_cases.submit_assignment.SubmitAssignmentOutputBoundary;

public class SubmitAssignmentPresenter {
    private String message;
    private boolean isSuccess;

    @Override
    public void presentSuccess() {
        message = "Assignment submitted successfully";
        isSuccess = true;
    }
    
    @Override
    public void presentError() {
        message = "Error submitting assignment";
        isSuccess = false;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
