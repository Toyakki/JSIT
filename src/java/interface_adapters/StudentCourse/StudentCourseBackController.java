package interface_adapters.StudentCourse;

import use_cases.StudentCourseBack.StudentCourseBackInputBoundary;
import use_cases.StudentCourseBack.StudentCourseBackUseCase;

public class StudentCourseBackController {
   private StudentCourseBackUseCase studentCourseBackUseCase;

   public StudentCourseBackController(StudentCourseBackUseCase studentCourseBackUseCase) {
       this.studentCourseBackUseCase = studentCourseBackUseCase;
   }

    public void back(String email) {
        studentCourseBackUseCase.goBack(email);
    }
}
