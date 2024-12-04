package use_cases.student_course_back_interactor;

import data_access.InMemoryUserDataAccessObject;
import data_access.UserDataAccessInterface;
import interface_adapters.ViewManagerModel;
import interface_adapters.student.StudentClassesViewModel;
import org.junit.jupiter.api.Test;
import use_cases.LoadDummyData;
import use_cases.UserOutputData;
import use_cases.student_course_back.StudentCourseBackInputBoundary;
import use_cases.student_course_back.StudentCourseBackOutputBoundary;
import use_cases.student_course_back.StudentCourseBackUseCase;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Tests StudentCourseBackInteractor with a student with 2 courses checking if the
// UserOutputData is formated as expected.
public class StudentCourseBackInteractorTest {
    @Test
    void create() {
        // Load Data
        UserDataAccessInterface userDsGateway = new InMemoryUserDataAccessObject();
        LoadDummyData loadDummyData = new LoadDummyData();
        loadDummyData.loadData(userDsGateway);
        // Creates an anonymous output boundary class with a modified output bound to test if the UserOutputData is formated as expected.
        StudentCourseBackInputBoundary interactor = getStudentCourseBackInputBoundary(userDsGateway);

        interactor.back("henrik-ibsen707@gmail.com");
    }

    private static StudentCourseBackInputBoundary getStudentCourseBackInputBoundary(UserDataAccessInterface userDsGateway) {
        StudentCourseBackOutputBoundary courseBackOutputBoundary = new StudentCourseBackOutputBoundary() {
            @Override
            public void prepareStudentCoursesView(UserOutputData outputData) {
                assertEquals("student", outputData.getType());
                assertEquals("henrik-ibsen707@gmail.com", outputData.getEmail());
                List<String> courseNames = new ArrayList<String>();
                courseNames.add("STA257");
                courseNames.add("CSC207");
                assertEquals(courseNames, outputData.getCourseNames());
            }
        };
        return new StudentCourseBackUseCase(userDsGateway, courseBackOutputBoundary);
    }
}
