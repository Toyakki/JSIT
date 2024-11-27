package use_cases.teacher_assignment_download;

public interface TeacherDownloadOutputBoundary {
    void presentSuccess(String message);
    void presentError(String message);
}
