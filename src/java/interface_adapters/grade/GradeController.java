package interface_adapters.grade;

import use_cases.grade.GradeUseCaseInteractor;

import java.io.File;

public class GradeController {
    private GradeUseCaseInteractor interactor;

    public GradeController(GradeUseCaseInteractor interactor) {
        this.interactor = interactor;
    }

    public void grade(String grade, File file, String studentEmail, String instructorEmail, String courseName, String assignmentName){
        interactor.grade(grade, file, studentEmail, instructorEmail, courseName, assignmentName);
    }
}
