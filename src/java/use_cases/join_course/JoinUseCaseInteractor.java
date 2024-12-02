package use_cases.join_course;

import data_access.UserDataAccessInterface;
import entities.Account;
import entities.Course;
import interface_adapters.join_class.JoinCoursePresenter;
import use_cases.UserOutputData;

public class JoinUseCaseInteractor {
    private final JoinCoursePresenter presenter;
    private final UserDataAccessInterface userdataAccess;

    public JoinUseCaseInteractor(JoinCoursePresenter presenter, UserDataAccessInterface userdataAccess) {
        this.presenter = presenter;
        this.userdataAccess = userdataAccess;
    }

    public void joinCourse(String studentEmail, String courseCode) {
        String instructorEmail = "";
        String courseName = "";
        for (Account user : userdataAccess.getAllUsers()){
            for (Course course : user.getCourses()){
                if (course.getCourseCode().equals(courseCode)){
                    instructorEmail = user.getEmail();
                    courseName = course.getName();
                }
            }
        }

        if (instructorEmail.isEmpty()){
            presenter.prepareFailView("Course does not exist");
            return;
        }

        Account teacher = userdataAccess.getUserByEmail(instructorEmail);

        Account student = this.userdataAccess.getUserByEmail(studentEmail);
//        if (!userdataAccess.userExistsByEmail(instructorEmail)) {
//            presenter.prepareFailView("Invalid instructor!");
//            return;
//        }
//        Account teacher = this.userdataAccess.getUserByEmail(instructorEmail);
//        if (!teacher.hasCourse(courseName)) {
//            presenter.prepareFailView("Invalid course!");
//            return;
//        }
        Course course = teacher.getCourseByName(courseName);
        student.addCourse(course);
        presenter.prepareSuccessView(new UserOutputData(studentEmail, "student", student.getCourseNames()));
    }
}
