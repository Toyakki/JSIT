package use_cases.grade;

import data_access.FileDataAccessInterface;
import data_access.UserDataAccessInterface;
import entities.Assignment;
import entities.Course;
import entities.PDFFile;
import entities.Submission;
import interface_adapters.grade.GradePresenter;
import interface_adapters.teacher_course.TeacherCourseState;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradeUseCaseInteractor {
    private GradePresenter presenter;
    private FileDataAccessInterface fileAccess;
    private UserDataAccessInterface userAccess;

    public GradeUseCaseInteractor(GradePresenter presenter,
                                  UserDataAccessInterface userAccess,
                                  FileDataAccessInterface fileAccess) {
        this.presenter = presenter;
        this.userAccess = userAccess;
        this.fileAccess = fileAccess;
    }

    public void grade(String grade, File file, String studentEmail, String instructorEmail, String courseName, String assignmentName){
        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            PDFFile encodedFile = new PDFFile(
                    file.getName(),
                    courseName + "/assignments/" + assignmentName + "/" + studentEmail + "/feedback",
                    fileBytes
            );
            fileAccess.saveFile(encodedFile);

            Course course = userAccess.getUserByEmail(instructorEmail).getCourseByName(courseName);
            Assignment gradedAssignment = course.getAssignmentByName(assignmentName);
            gradedAssignment.setMarksReceived(studentEmail);

            Submission submission = gradedAssignment.getSubmission(studentEmail);
            submission.setGrade(grade);
            submission.setFeedbackFile(encodedFile);

            List<String> studentEmails = course.getStudentEmails();

            List<Assignment> assignments = course.getAssignments();
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
                for (String student : studentEmails) {
                    if (assignment.getSubmission(studentEmail).isGraded()){
                        markMap.put(student, assignment.getSubmission(student).getGrade());
                    } else {
                        markMap.put(student, assignment.getMarks());
                    }
                    stageMap.put(student, assignment.getSubmission(student).getStage());
                    markReceivedMap.put(student, assignment.getSubmission(student).getStage());
                }
                assignmentMarks.add(markMap);
                assignmentStages.add(stageMap);
                assignmentMarksReceived.add(markReceivedMap);
            }

            TeacherCourseState state = new TeacherCourseState(
                    courseName,
                    instructorEmail,
                    assignmentNames,
                    assignmentDueDates,
                    assignmentMarks,
                    assignmentStages,
                    assignmentMarksReceived,
                    assignmentBaseMarks,
                    studentEmails,
                    course.getCourseCode()
            );
            this.presenter.refreshView(state);

        } catch (IOException e) {
            presenter.prepareFailView("Could not read feedback file");
        }
    }
}
