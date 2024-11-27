package use_cases.student_course_back;

import data_access.UserDataAccessInterface;
import use_cases.UserOutputData;

import java.util.ArrayList;
import java.util.List;

public class StudentCourseBackUseCase implements StudentCourseBackInputBoundary {
    final UserDataAccessInterface userDsGateway;
    final StudentCourseBackOutputBoundary outputBoundary;

    public StudentCourseBackUseCase(UserDataAccessInterface userDsGateway, StudentCourseBackOutputBoundary outputBoundary) {
        this.userDsGateway = userDsGateway;
        this.outputBoundary = outputBoundary;
    }

    public void back(String email) {
        List<String> courses = new ArrayList<>();
        for (int i = 0; i < userDsGateway.getUserByEmail(email).getCourseNames().size(); i++) {
            courses.add(userDsGateway.getUserByEmail(email).getCourseNames().get(i));
        }
        UserOutputData userOutputData = new UserOutputData(email,"student", courses);
        this.outputBoundary.prepareStudentCoursesView(userOutputData);
    }
}
