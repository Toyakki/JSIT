package interface_adapters.StudentCourse;

import use_cases.StudentCourseBack.StudentCourseBackUseCase;

public class StudentCourseBackController {
    private StudentCourseBackUseCase studentBackUseCase;

    public StudentCourseBackController(StudentCourseBackUseCase studentCourseBackUseCase) {
        this.studentBackUseCase = studentCourseBackUseCase;
    }

    public void back(String email) {
        studentBackUseCase.back(email);
    }
}
