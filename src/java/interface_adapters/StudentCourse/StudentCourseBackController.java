package interface_adapters.StudentCourse;

import use_cases.StudentCourseBack.StudentCourseBackUseCase;

public class StudentCourseBackController {
    public void back(String email) {
        StudentCourseBackUseCase.goBack(email);
    }
}
