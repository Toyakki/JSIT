package interface_adapters.StudentCourse;

import interface_adapters.ViewModel;

public class StudentCourseViewModel extends ViewModel<StudentCourseState> {
    public StudentCourseViewModel() {
        super("student course");
        setState(new StudentCourseState());
    }
}
