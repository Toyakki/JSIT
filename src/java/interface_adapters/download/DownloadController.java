package interface_adapters.download;


import use_cases.download.DownloadInputBoundary;

public class DownloadController {
    private final DownloadInputBoundary teacherDownloadInputBoundary;

    public DownloadController(DownloadInputBoundary teacherDownloadInputBoundary) {
        this.teacherDownloadInputBoundary = teacherDownloadInputBoundary;
    }

    public void handleTeacherSubmitted(String courseName, String assignmentName, String studentEmail){
        teacherDownloadInputBoundary.downloadWrittenAssignment(
                courseName,
                assignmentName,
                studentEmail
        );
    }

    public void handleDownloadFeedback(String courseName, String assignmentName, String studentEmail){
        teacherDownloadInputBoundary.downloadFeedback(courseName, assignmentName, studentEmail);
    }

    public void handleDownloadOriginal(String courseName, String assignmentName){
        teacherDownloadInputBoundary.downloadOriginal(courseName, assignmentName);
    }
}