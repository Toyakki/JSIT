package use_cases.StudentCourseBack;

import data_access.UserDataAccessInterface;
import java.util.ArrayList;
import java.util.List;

public class StudentCourseBackUseCase {
    final UserDataAccessInterface userDsGateway;

    public StudentCourseBackUseCase(UserDataAccessInterface userDsGateway) {
        this.userDsGateway = userDsGateway;
    }

    public void back(String email) {
        List<String> courses = new ArrayList<>();
        for (int i = 0; i < userDsGateway.getUserByEmail(email).getCourseNames().size(); i++) {
            courses.add(userDsGateway.getUserByEmail(email).getCourseNames().get(i));
        }
        // call output boundary
    }
}
