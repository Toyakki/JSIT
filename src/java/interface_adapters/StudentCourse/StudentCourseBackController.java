package interface_adapters.StudentCourse;

import use_cases.StudentCourseBack.StudentCourseBackInputBoundary;
import use_cases.StudentCourseBack.StudentCourseBackUseCase;

public class StudentCourseBackController {
   private StudentCourseBackInputBoundary studentCourseBackInputBoundary;

   public StudentCourseBackController(StudentCourseBackInputBoundary studentCourseBackInputBoundary) {
       this.studentCourseBackInputBoundary = studentCourseBackInputBoundary;
   }

    public void back(String email) {
        studentCourseBackInputBoundary.back(email);
    }
}
