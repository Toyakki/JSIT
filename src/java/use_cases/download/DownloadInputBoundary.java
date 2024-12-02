package use_cases.download;

public interface DownloadInputBoundary {
    void downloadWrittenAssignment(String courseName, String assignmentName, String studentEmail);

    void downloadFeedback(String courseName, String assignmentName, String studentEmail);
}