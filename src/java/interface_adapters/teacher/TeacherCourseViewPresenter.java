package interface_adapters.teacher;

import interface_adapters.teacher_course.TeacherCourseState;
import interface_adapters.teacher_course.TeacherCourseViewModel;
import interface_adapters.ViewManagerModel;

import java.util.List;

public class TeacherCourseViewPresenter {
    private TeacherClassesViewModel teacherClassesViewModel;
    private TeacherCourseViewModel courseViewModel;
    private ViewManagerModel viewManagerModel;

    public TeacherCourseViewPresenter(TeacherClassesViewModel teacherClassesViewModel,
                                      TeacherCourseViewModel teacherCourseViewModel,
                                      ViewManagerModel viewManagerModel) {
        this.teacherClassesViewModel = teacherClassesViewModel;
        this.courseViewModel = teacherCourseViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    public void prepareView(String email,
                            String courseName,
                            List<String> assignmentNames,
                            List<String> assignmentDueDates,
                            List<String> assignmentMarks,
                            List<String> assignmentStages,
                            List<String> assignmentMarksReceived,
                            List<String> studentEmails) {
        final TeacherCourseState state = new TeacherCourseState(
                courseName,
                email,
                assignmentNames,
                assignmentDueDates,
                assignmentMarks,
                assignmentStages,
                assignmentMarksReceived,
                studentEmails
        );
        this.courseViewModel.setState(state);
        this.courseViewModel.firePropertyChanged();

        this.viewManagerModel.setState(courseViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }
}
