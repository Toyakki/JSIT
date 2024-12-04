package use_cases.create_assignment;

import data_access.FileDataAccessInterface;
import data_access.UserDataAccessInterface;
import entities.Account;
import entities.Assignment;
import entities.Course;
import entities.PDFFile;
import interface_adapters.create_assignment.CreateAssignmentPresenter;
import interface_adapters.teacher_course.TeacherCourseState;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateAssignmentInteractor implements CreateAssignmentInputBoundary {
    private final UserDataAccessInterface userDataAccessInterface;
    private final FileDataAccessInterface fileDataAccessInterface;
    private final CreateAssignmentOutputBoundary presenter;

    public CreateAssignmentInteractor(
            CreateAssignmentOutputBoundary presenter,
            UserDataAccessInterface userDataAccessInterface,
            FileDataAccessInterface fileDataAccessInterface
    ) {
        this.presenter = presenter;
        this.userDataAccessInterface = userDataAccessInterface;
        this.fileDataAccessInterface = fileDataAccessInterface;
    }

    public void createAssignment(
            String email,
            String assignmentName,
            String courseName,
            String dueDate,
            String marks,
            File rawAssignmentFile
    ) {
        try {

            byte[] assignmentBytes = Files.readAllBytes(rawAssignmentFile.toPath());
            // temporary naming scheme
            PDFFile encodedAssignmentFile = new PDFFile(
                    assignmentName,
                    courseName + "/assignments/" + assignmentName,
                    assignmentBytes
            );

            Course course = userDataAccessInterface.getUserByEmail(email).getCourseByName(courseName);

            Assignment newAssignment = new Assignment(
                    course,
                    assignmentName,
                    dueDate,
                    marks,
                    "assigned",
                    "false"
            );

            course.addAssignment(newAssignment);

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

            // upload assignmentBytes
            fileDataAccessInterface.saveFile(encodedAssignmentFile);

            TeacherCourseState state = new TeacherCourseState(
                    courseName,
                    email,
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
//            throw new RuntimeException("File could not be read"); // replace with actual error handling
            this.presenter.prepareErrorView("File is missing or could not be read");
        }
    }
}
