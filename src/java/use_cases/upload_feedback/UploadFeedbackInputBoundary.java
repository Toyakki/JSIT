package use_cases.upload_feedback;

import java.io.File;

public interface UploadFeedbackInputBoundary {
    public void uploadFeedback(File feedback, String nameCourse, String email, String nameAssignment);
}
