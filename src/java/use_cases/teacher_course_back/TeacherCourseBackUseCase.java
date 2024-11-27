package use_cases.teacher_course_back;

import data_access.UserDataAccessInterface;
import use_cases.UserOutputData;

import java.util.ArrayList;
import java.util.List;

public class TeacherCourseBackUseCase implements TeacherCourseBackInputBoundary{
    final UserDataAccessInterface userDsGateway;
    final TeacherCourseBackOutputBoundary outputBoundary;

    public TeacherCourseBackUseCase(UserDataAccessInterface userDsGateway,
                                    TeacherCourseBackOutputBoundary outputBoundary) {
        this.userDsGateway = userDsGateway;
        this.outputBoundary = outputBoundary;
    }

    public void back(String email) {
        List<String> courses = new ArrayList<>();
        for (int i = 0; i < userDsGateway.getUserByEmail(email).getCourseNames().size(); i++) {
            courses.add(userDsGateway.getUserByEmail(email).getCourseNames().get(i));
        }
        UserOutputData userOutputData = new UserOutputData(email,"student", courses);
        this.outputBoundary.prepareTeacherCoursesView(userOutputData);
    }


}
