package use_cases.teacher_assignment_download;

import interface_adapters.teacher_course.TeacherCourseState;

import java.util.List;

public interface TeacherDownloadOutputBoundary {
    void presentSuccess(
            String courseName,
            String teacherEmail,
            String assignmentNames,
            String assignmentEmails,
            String assignmentDueDates,
            String assignmentMarks,
            String assignmentStages,
            String assignmentMarksReceived
    );
    void presentError(String messsage);
}