package interface_adapters.TeacherCourse;

import use_cases.TeacherCourseBack.TeacherCourseBackInputBoundary;

public class TeacherCourseBackController {
    TeacherCourseBackInputBoundary teacherCourseBackInputBoundary;

    public TeacherCourseBackController(TeacherCourseBackInputBoundary teacherCourseBackInputBoundary) {
        this.teacherCourseBackInputBoundary = teacherCourseBackInputBoundary;
    }

    public void back(String email) {teacherCourseBackInputBoundary.back(email);}
}
