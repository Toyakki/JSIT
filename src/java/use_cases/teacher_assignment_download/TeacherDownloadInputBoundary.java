package use_cases.teacher_assignment_download;

import com.dropbox.core.DbxException;

public interface TeacherDownloadInputBoundary {
    void downloadWrittenAssignment(String courseName, String assignmentName, String teacherEmail, String studentEmail);
}
