package interface_adapters.student_course;

import interface_adapters.ViewModel;

public class StudentCourseViewModel extends ViewModel<StudentCourseState> {
    public StudentCourseViewModel() {
        super("student course");
        setState(new StudentCourseState());
    }
}
