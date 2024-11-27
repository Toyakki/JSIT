package interface_adapters.student;

import interface_adapters.student_course.StudentCourseState;
import interface_adapters.student_course.StudentCourseViewModel;
import interface_adapters.ViewManagerModel;

import java.util.List;

public class StudentCourseViewPresenter {
    private StudentClassesViewModel studentClassesViewModel;
    private StudentCourseViewModel courseViewModel;
    private ViewManagerModel viewManagerModel;

    public StudentCourseViewPresenter(
            StudentClassesViewModel studentClassesViewModel,
            StudentCourseViewModel courseViewModel,
            ViewManagerModel viewManagerModel
    ) {
        this.studentClassesViewModel = studentClassesViewModel;
        this.courseViewModel = courseViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    // will probably end up replacing this with dedicated boundary data
    public void prepareView(String email,
                            String courseName,
                            List<String> assignmentNames,
                            List<String> assignmentDueDates,
                            List<String> assignmentMarks,
                            List<String> assignmentStages,
                            List<String> assignmentMarksReceived
    ){
        final StudentCourseState state = new StudentCourseState(
                email,
                courseName,
                assignmentNames,
                assignmentDueDates,
                assignmentMarks,
                assignmentStages,
                assignmentMarksReceived
        );
        this.courseViewModel.setState(state);
        this.courseViewModel.firePropertyChanged();

        this.viewManagerModel.setState(courseViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }
}
