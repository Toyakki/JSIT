package use_cases.teacher_course_back_interactor;

import data_access.InMemoryUserDataAccessObject;
import data_access.UserDataAccessInterface;
import interface_adapters.ViewManagerModel;
import interface_adapters.teacher.TeacherClassesViewModel;
import interface_adapters.teacher.TeacherCoursesPresenter;
import org.junit.jupiter.api.Test;
import use_cases.LoadDummyData;
import use_cases.UserOutputData;
import use_cases.teacher_course_back.TeacherCourseBackInputBoundary;
import use_cases.teacher_course_back.TeacherCourseBackOutputBoundary;
import use_cases.teacher_course_back.TeacherCourseBackUseCase;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Tests TeacherCourseBackInteractor with a teacher with 2 courses checking if the UserOutputData is formated as expected.
public class TeacherCourseBackInteractorTest {

    @Test
    void create() {
        // Load Data
        UserDataAccessInterface userDsGateway = new InMemoryUserDataAccessObject();
        LoadDummyData loadDummyData = new LoadDummyData();
        loadDummyData.loadData(userDsGateway);
        // Create classes needed to run the use case
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        TeacherClassesViewModel teacherClassesViewModel = new TeacherClassesViewModel();
        TeacherCourseBackOutputBoundary teacherCourseBackOutputBoundary = new TeacherCoursesPresenter(viewManagerModel, teacherClassesViewModel);
        // Creates an anonymous output boundary class with a modified output bound to test if the UserOutputData is formated as expected.
        TeacherCourseBackInputBoundary interactor = getTeacherCourseBackInputBoundary(userDsGateway);

        interactor.back("joerepka@mail.utoronto.ca");
    }

    private static TeacherCourseBackInputBoundary getTeacherCourseBackInputBoundary(UserDataAccessInterface userDsGateway) {
            TeacherCourseBackOutputBoundary courseBackOutputBoundary = new TeacherCourseBackOutputBoundary() {
            @Override
            public void prepareTeacherCoursesView(UserOutputData userOutputData) {
                assertEquals("teacher", userOutputData.getType());
                assertEquals("joerepka@mail.utoronto.ca", userOutputData.getEmail());
                List<String> courseNames = new ArrayList<String>();
                courseNames.add("MAT347");
                courseNames.add("MAT348");
                assertEquals(courseNames, userOutputData.getCourseNames());
            }
        };
        return new TeacherCourseBackUseCase(userDsGateway, courseBackOutputBoundary);
    }
}
