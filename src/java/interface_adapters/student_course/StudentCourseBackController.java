package interface_adapters.student_course;

import use_cases.student_course_back.StudentCourseBackInputBoundary;

public class StudentCourseBackController {
   private StudentCourseBackInputBoundary studentCourseBackInputBoundary;

   public StudentCourseBackController(StudentCourseBackInputBoundary studentCourseBackInputBoundary) {
       this.studentCourseBackInputBoundary = studentCourseBackInputBoundary;
   }

    public void back(String email) {
        studentCourseBackInputBoundary.back(email);
    }
}
