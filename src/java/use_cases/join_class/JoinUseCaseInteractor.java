package use_cases.join_class;

import data_access.CourseDataAccessInterface;
import data_access.UserDataAccessInterface;
import entities.Account;
import entities.Course;
import interface_adapters.join_class.JoinCoursePresenter;
import use_cases.UserOutputData;

public class JoinUseCaseInteractor {
    private JoinCoursePresenter presenter;
    private UserDataAccessInterface userdataAccess;
    private CourseDataAccessInterface coursedataAccess;

    public JoinUseCaseInteractor(JoinCoursePresenter presenter, UserDataAccessInterface userdataAccess) {
        this.presenter = presenter;
        this.userdataAccess = userdataAccess;
    }

    public void joinCourse(String email, String courseCode) {
        Account user = this.userdataAccess.getUserByEmail(email);
        if (!coursedataAccess.existsByCode(courseCode)) {
            presenter.prepareFailView("Course does not exist");
        }
        Course course = this.coursedataAccess.getCourseByCode(courseCode);
        user.addCourse(course);
        presenter.prepareSuccessView(new UserOutputData(email, "student", user.getCourseNames()));
        }
    }
}
