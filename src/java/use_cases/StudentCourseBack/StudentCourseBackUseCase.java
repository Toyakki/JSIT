package use_cases.StudentCourseBack;

import data_access.UserDataAccessInterface;
import use_cases.UserOutputData;

import java.util.ArrayList;
import java.util.List;

public class StudentCourseBackUseCase implements StudentCourseBackInputBoundary{
    private UserDataAccessInterface userDsGateway;
    private StudentCourseBackOutputBoundary outputBoundary;

    public StudentCourseBackUseCase(UserDataAccessInterface userDsGateway,
                                    StudentCourseBackOutputBoundary outputBoundary) {
        this.userDsGateway = userDsGateway;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void goBack(String email) {
        List<String> courses = new ArrayList<>();
        for (int i = 0; i < userDsGateway.getUserByEmail(email).getCourseNames().size(); i++) {
            courses.add(userDsGateway.getUserByEmail(email).getCourseNames().get(i));
        }
        UserOutputData userOutputData = new UserOutputData(email, "student", courses);
        outputBoundary.prepareStudentCourseView(userOutputData);
    }
}
