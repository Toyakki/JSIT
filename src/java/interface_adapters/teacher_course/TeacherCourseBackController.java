package interface_adapters.teacher_course;

import use_cases.teacher_course_back.TeacherCourseBackInputBoundary;

public class TeacherCourseBackController {
    TeacherCourseBackInputBoundary teacherCourseBackInputBoundary;

    public TeacherCourseBackController(TeacherCourseBackInputBoundary teacherCourseBackInputBoundary) {
        this.teacherCourseBackInputBoundary = teacherCourseBackInputBoundary;
    }

    public void back(String email) {teacherCourseBackInputBoundary.back(email);}
}
