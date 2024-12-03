package use_cases.grade;

import data_access.FileDataAccessInterface;
import data_access.InMemoryFileDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.UserDataAccessInterface;
import interface_adapters.ViewModel;
import interface_adapters.teacher_course.TeacherCourseState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_cases.LoadDummyData;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GradeInteractorTest {

    @Test
    void success() {
        UserDataAccessInterface userDsGateway = new InMemoryUserDataAccessObject();
        LoadDummyData loadDummyData = new LoadDummyData();
        loadDummyData.loadData(userDsGateway);
        FileDataAccessInterface fileDataAccessInterface = new InMemoryFileDataAccessObject();
        GradeInputBoundary interactor = getGradeInputBoundary(fileDataAccessInterface, userDsGateway);
        File file = new File("src/test/use_cases/grade/test.pdf");
        interactor.gradeAssignment("24", file, "henrik-ibsen707@gmail.com",
                "t", "CSC207", "Final Project");
    }

    @Test
    void fail() {
        UserDataAccessInterface userDsGateway = new InMemoryUserDataAccessObject();
        LoadDummyData loadDummyData = new LoadDummyData();
        loadDummyData.loadData(userDsGateway);
        FileDataAccessInterface fileDataAccessInterface = new InMemoryFileDataAccessObject();
        GradeInputBoundary interactor = getGradeInputBoundary(fileDataAccessInterface, userDsGateway);
        File file = new File("test.pd");
        interactor.gradeAssignment("24", file, "henrik-ibsen707@gmail.com",
                "t", "CSC207", "Final Project");
    }

    private static GradeInputBoundary getGradeInputBoundary(FileDataAccessInterface fileAccess,
                                                            UserDataAccessInterface userAccess) {
        GradeOutputBoundary gradeOutputBoundary = new GradeOutputBoundary() {
            @Override
            public void refreshView(TeacherCourseState state) {
               assertEquals("CSC207", state.getCourseName());
               assertEquals("t", state.getEmail());
               List<String> assignmentNames = new ArrayList<>();
               assignmentNames.add("Final Project");
               assignmentNames.add("Mini Project");
               assertEquals(assignmentNames, state.getAssignmentsNames());
               List<String> assignmentDueDates = new ArrayList<>();
               assignmentDueDates.add("December 4th");
               assignmentDueDates.add("December 3rd");
               assertEquals(assignmentDueDates, state.getAssignmentsDueDates());

               Map<String, String> assignmentStages = new HashMap<>();
               assignmentStages.put("henrik-ibsen707@gmail.com", "graded");
               assignmentStages.put("sark@gmail.com", "not submitted");
               assertEquals(assignmentStages, state.getAssignmentsStages().getFirst());


                Map<String, String> assignmentMarks = new HashMap<>();
                assignmentMarks.put("henrik-ibsen707@gmail.com", "24");
                assignmentMarks.put("sark@gmail.com", "ungraded");
                assertEquals(assignmentMarks, state.getAssignmentsMarks().getFirst());

                List<String> assignmentBaseMarks = new ArrayList<>();
                assignmentBaseMarks.add("25");
                assignmentBaseMarks.add("10");
                assertEquals(assignmentBaseMarks, state.getAssignmentBaseMarks());
                List<String> studentEmails = new ArrayList<>();
                studentEmails.add("henrik-ibsen707@gmail.com");
                studentEmails.add("sark@gmail.com");
                assertEquals(studentEmails, state.getStudentEmails());
            }

            @Override
            public void prepareFailView(String message) {
                assertEquals("Could not read feedback file", message);
            }
        };

    return new GradeInteractor(fileAccess, gradeOutputBoundary, userAccess);
    }

}
