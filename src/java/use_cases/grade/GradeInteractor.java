package use_cases.grade;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data_access.FileDataAccessInterface;
import data_access.UserDataAccessInterface;
import entities.Assignment;
import entities.Course;
import entities.PDFFile;
import entities.Submission;
import interface_adapters.teacher_course.TeacherCourseState;

public class GradeInteractor implements GradeInputBoundary {
    private final FileDataAccessInterface fileAccess;
    private final GradeOutputBoundary gradeOutputBoundary;
    private final UserDataAccessInterface userAccess;

    public GradeInteractor(FileDataAccessInterface fileAccess, GradeOutputBoundary gradeOutputBoundary, UserDataAccessInterface userAccess) {
        this.fileAccess = fileAccess;
        this.gradeOutputBoundary = gradeOutputBoundary;
        this.userAccess = userAccess;
    }

    @Override
    public void gradeAssignment(String grade, File file, String studentEmail,
                                String teacherEmail, String courseName, String assignmentName) {
        try {
            // Saves file in byte form
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            System.out.println(courseName + "/assignments/" + assignmentName + "/" + studentEmail + "/feedback");
            PDFFile pdfFile = new PDFFile(
                    assignmentName + " _feedback.pdf",
                    courseName + "/" + assignmentName + "/" + studentEmail + "/feedback/" + assignmentName + "_feedback.pdf",
                    fileBytes);
            fileAccess.saveFile(pdfFile);

            // Sets Submission with feedback
            Course course = userAccess.getUserByEmail(teacherEmail).getCourseByName(courseName);
            Assignment gradedAssignment = course.getAssignmentByName(assignmentName);
            Submission submission = gradedAssignment.getSubmission(studentEmail);
            submission.setGrade(grade);
            submission.setFeedbackFile(pdfFile);

            // Creates new state
            List<String> studentEmails = course.getStudentEmails();
            List<Assignment> assignments = course.getAssignments();
            List<String> assignmentNames = new ArrayList<>();
            List<String> assignmentDueDates = new ArrayList<>();
            List<Map<String, String>> assignmentMarks = new ArrayList<>();
            List<Map<String, String>> assignmentStages = new ArrayList<>();
            List<Map<String, String>> assignmentMarksReceived = new ArrayList<>();
            List<String> assignmentBaseMarks = new ArrayList<>();

            // Mutates states with new info
            for (Assignment assignment : assignments) {
                assignmentNames.add(assignment.getName());
                assignmentDueDates.add(assignment.getDueDate());
                assignmentBaseMarks.add(assignment.getMarks());
                Map<String, String> markMap = new HashMap<>();
                Map<String, String> stageMap = new HashMap<>();
                Map<String, String> markReceivedMap = new HashMap<>();
                for (String student : studentEmails) {
                    if (assignment.getSubmission(studentEmail).isGraded()) {
                        markMap.put(student, assignment.getSubmission(student).getGrade());
                    }
                    else {
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
                    teacherEmail,
                    assignmentNames,
                    assignmentDueDates,
                    assignmentMarks,
                    assignmentStages,
                    assignmentMarksReceived,
                    assignmentBaseMarks,
                    studentEmails,
                    course.getCourseCode()
            );

            this.gradeOutputBoundary.refreshView(state);
        }
        catch (IOException e) {
            gradeOutputBoundary.prepareFailView("Could not read feedback file");
        }

    }
}
