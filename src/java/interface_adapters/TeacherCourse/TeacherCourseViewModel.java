package interface_adapters.TeacherCourse;

import interface_adapters.ViewModel;

public class TeacherCourseViewModel extends ViewModel<TeacherCourseState> {
    public TeacherCourseViewModel() {
        super("teacher course");
        setState(new TeacherCourseState());
    }
}