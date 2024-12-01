package use_cases.student_course_selection;

import data_access.FileDataAccessInterface;
import data_access.UserDataAccessInterface;
import entities.Account;
import entities.Assignment;
import entities.PDFFile;
import interface_adapters.student.StudentCourseViewPresenter;

import java.util.ArrayList;
import java.util.List;

public class StudentCourseViewInteractor {
    private StudentCourseViewPresenter presenter;
    private FileDataAccessInterface fileDataAccess;
    private UserDataAccessInterface userDataAccess;

    // eventually replace all presenters and managers with proper interfaces for flexability
    public StudentCourseViewInteractor(StudentCourseViewPresenter presenter,
                                       FileDataAccessInterface fileDataAccess,
                                       UserDataAccessInterface userDataAccess
    ) {
        this.presenter = presenter;
        this.fileDataAccess = fileDataAccess;
        this.userDataAccess = userDataAccess;
    }

    public void viewCourse(
            String email,
            String courseName
    ){
        // temporary path strategy
        List<PDFFile> assignmentFiles = this.fileDataAccess.getFiles(courseName + "/assignments");
        Account user = userDataAccess.getUserByEmail(email);
        List<Assignment> assignments = user.getCourseByName(courseName).getAssignments();
        List<String> assignmentNames = new ArrayList<>();
        List<String> assignmentDueDates = new ArrayList<>();
        List<String> assignmentMarks = new ArrayList<>();
        List<String> assignmentStages = new ArrayList<>();
        List<String> assignmentMarksReceived = new ArrayList<>();
        for (Assignment assignment : assignments) {
            assignmentNames.add(assignment.getName());
            assignmentDueDates.add(assignment.getDueDate());
            assignmentMarks.add(assignment.getMarks());
            assignmentStages.add(assignment.getStage());
            assignmentMarksReceived.add(assignment.getMarksReceivedStatus(email));
        }

        // should probably add a PDFFile wrapper class to avoid presenter being composed with an entity
        this.presenter.prepareView(
                email,
                courseName,
                assignmentNames,
                assignmentDueDates,
                assignmentMarks,
                assignmentStages,
                assignmentMarksReceived
        );
    }
}
