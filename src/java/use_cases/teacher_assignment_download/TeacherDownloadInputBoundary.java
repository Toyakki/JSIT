package use_cases.teacher_assignment_download;

public interface TeacherDownloadInputBoundary {
    void downloadWrittenAssignment(String courseName, String studentEmail);
}
