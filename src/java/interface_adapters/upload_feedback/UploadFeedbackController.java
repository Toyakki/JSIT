package interface_adapters.upload_feedback;

import use_cases.upload_feedback.UploadFeedbackInputBoundary;

import java.io.File;

public class UploadFeedbackController {
    private UploadFeedbackInputBoundary uploadFeedbackInputBoundary;

    public UploadFeedbackController(UploadFeedbackInputBoundary uploadFeedbackInputBoundary) {
        this.uploadFeedbackInputBoundary = uploadFeedbackInputBoundary;
    }

    public void setFeedback(File feedback, String nameCourse, String email, String nameAssignment) {
        uploadFeedbackInputBoundary.uploadFeedback(feedback, nameCourse, email, nameAssignment);
    }
}
