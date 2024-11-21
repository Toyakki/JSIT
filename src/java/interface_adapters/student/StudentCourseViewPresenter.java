package interface_adapters.student;

import interface_adapters.StudentCourse.StudentCourseState;
import interface_adapters.StudentCourse.StudentCourseViewModel;
import interface_adapters.ViewManagerModel;

import java.util.ArrayList;

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
    public void prepareView(String courseName){
        final StudentCourseState state = new StudentCourseState(
                courseName,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        this.courseViewModel.setState(state);
        this.courseViewModel.firePropertyChanged();

        this.viewManagerModel.setState(courseViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }
}
