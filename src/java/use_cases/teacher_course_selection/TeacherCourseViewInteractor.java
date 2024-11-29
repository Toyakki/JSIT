package use_cases.teacher_course_selection;

import data_access.FileDataAccessInterface;
import data_access.UserDataAccessInterface;
import entities.Account;
import entities.Assignment;
import entities.Course;
import entities.PDFFile;
import interface_adapters.teacher.TeacherCourseViewPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherCourseViewInteractor {
    private TeacherCourseViewPresenter presenter;
    private final FileDataAccessInterface fileDataAccess;
    private final UserDataAccessInterface userDataAccess;

    public TeacherCourseViewInteractor(TeacherCourseViewPresenter presenter,
                                       FileDataAccessInterface fileDataAccess,
                                       UserDataAccessInterface userDataAccess) {
        this.presenter = presenter;
        this.fileDataAccess = fileDataAccess;
        this.userDataAccess = userDataAccess;
    }

    public void viewCourse(String email, String courseName) {
        // temporary path strategy
        List<PDFFile> assignmentFiles = this.fileDataAccess.getFiles(courseName + "/assignments");
        Account user = userDataAccess.getUserByEmail(email);
        Course course = user.getCourseByName(courseName);

        List<String> studentEmails = course.getStudentEmails();

        List<Assignment> assignments = user.getCourseByName(courseName).getAssignments();
        List<String> assignmentNames = new ArrayList<>();
        List<String> assignmentDueDates = new ArrayList<>();
        List<Map<String, String>> assignmentMarks = new ArrayList<>();
        List<Map<String, String>> assignmentStages = new ArrayList<>();
        List<Map<String, String>> assignmentMarksReceived = new ArrayList<>();
        List<String> assignmentBaseMarks = new ArrayList<>();

        for (Assignment assignment : assignments) {
            assignmentNames.add(assignment.getName());
            assignmentDueDates.add(assignment.getDueDate());
            assignmentBaseMarks.add(assignment.getMarks());
            Map<String, String> markMap = new HashMap<>();
            Map<String, String> stageMap = new HashMap<>();
            Map<String, String> markReceivedMap = new HashMap<>();
            for (String studentEmail : studentEmails) {
                if (assignment.getSubmission(studentEmail).isGraded()){
                    markMap.put(studentEmail, assignment.getSubmission(studentEmail).getGrade());
                } else {
                    markMap.put(studentEmail, assignment.getMarks());
                }
                stageMap.put(studentEmail, assignment.getSubmission(studentEmail).getStage());
                markReceivedMap.put(studentEmail, assignment.getSubmission(studentEmail).getStage());
            }
            assignmentMarks.add(markMap);
            assignmentStages.add(stageMap);
            assignmentMarksReceived.add(markReceivedMap);
        }

        // should probably add a PDFFile wrapper class to avoid presenter being composed with an entity
        this.presenter.prepareView(
                email,
                courseName,
                assignmentNames,
                assignmentDueDates,
                assignmentMarks,
                assignmentStages,
                assignmentMarksReceived,
                assignmentBaseMarks,
                studentEmails
        );
    }
}
