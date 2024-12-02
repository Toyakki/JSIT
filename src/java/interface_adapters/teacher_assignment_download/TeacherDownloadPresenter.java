package interface_adapters.teacher_assignment_download;
import interface_adapters.teacher_course.TeacherCourseState;
import interface_adapters.teacher_course.TeacherCourseViewModel;
import use_cases.teacher_assignment_download.TeacherDownloadOutputBoundary;

import java.util.ArrayList;
import java.util.List;

public class DownloadPresenter implements TeacherDownloadOutputBoundary {
    private final TeacherCourseViewModel viewModel;

    public DownloadPresenter(TeacherCourseViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(String courseName,
                               String teacherEmail,
                               String assignmentName,
                               String assignmentEmail,
                               String assignmentDueDate,
                               String assignmentMark,
                               String assignmentStage,
                               String assignmentMarksReceivedStatus) {

        List<String> assignmentNames = new ArrayList<>();
        List<String> assignmentEmails = new ArrayList<>();
        List<String> assignmentDueDates = new ArrayList<>();
        List<String> assignmentMarks = new ArrayList<>();
        List<String> assignmentStages = new ArrayList<>();
        List <String> assignmentMarksReceived = new ArrayList<>();

        assignmentNames.add(assignmentName);
        assignmentEmails.add(assignmentEmail);
        assignmentDueDates.add(assignmentDueDate);
        assignmentMarks.add(assignmentMark);
        assignmentStages.add(assignmentStage);
        assignmentMarksReceived.add(assignmentMarksReceivedStatus);

        TeacherCourseState teacherCourseState = new TeacherCourseState(
                courseName,
                teacherEmail,
                assignmentNames,
                assignmentEmails,
                assignmentDueDates,
                assignmentMarks,
                assignmentStages,
                assignmentMarksReceived
        );
        this.viewModel.setState(teacherCourseState);
        this.viewModel.firePropertyChanged();
    }

    @Override
    public void presentError(String messsage) {
//        this.viewModel.setError(message);
        this.viewModel.firePropertyChanged();
    }
}
