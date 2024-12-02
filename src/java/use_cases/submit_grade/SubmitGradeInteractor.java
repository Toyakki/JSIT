package use_cases.submit_grade;

import data_access.UserDataAccessInterface;
import entities.Account;
import entities.Assignment;
import entities.Course;
import entities.Submission;

import java.util.List;

public class SubmitGradeInteractor implements SubmitGradeInputBoundary{
    private UserDataAccessInterface userDataAccessInterface;

    public SubmitGradeInteractor(UserDataAccessInterface userDataAccessInterface) {
        this.userDataAccessInterface = userDataAccessInterface;
    }

    @Override
    public void submitGrade(String grade, String nameCourse, String email, String nameAssignment) {
        Account user = userDataAccessInterface.getUserByEmail(email);
        Course course = user.getCourseByName(nameCourse);
        Assignment assignment = course.getAssignmentByName(nameAssignment);
        assignment.setMarks(grade);
        //TODO upload
    }
}
