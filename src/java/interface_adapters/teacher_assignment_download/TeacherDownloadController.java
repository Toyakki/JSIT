package interface_adapters.teacher_assignment_download;


import use_cases.teacher_assignment_download.TeacherDownloadInputBoundary;

public class TeacherDownloadController {
    private final TeacherDownloadInputBoundary teacherDownloadInputBoundary;

    public TeacherDownloadController(TeacherDownloadInputBoundary teacherDownloadInputBoundary) {
        this.teacherDownloadInputBoundary = teacherDownloadInputBoundary;
    }

    public void handle_teacherDownload(String courseName, String assignmentName, String teacherEmail, String studentEmail){
        teacherDownloadInputBoundary.downloadWrittenAssignment(
                courseName,
                assignmentName,
                teacherEmail,
                studentEmail
        );
    }
}