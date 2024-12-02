package use_cases.create_course;

import data_access.UserDataAccessInterface;
import entities.Account;
import entities.Course;
import entities.CourseFactory;
import interface_adapters.create_class.CreateCoursePresenter;
import use_cases.UserOutputData;

public class CreateCourseUseCaseInteractor {
    private CreateCoursePresenter presenter;
    private UserDataAccessInterface dataAccess;
    private CourseFactory courseFactory;

    public CreateCourseUseCaseInteractor(CreateCoursePresenter presenter, UserDataAccessInterface dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }
    public void createCourse(String email, String classname) {
        Account user = this.dataAccess.getUserByEmail(email);
        Course course = this.courseFactory.createClass(user.getEmail(), classname);
        user.addCourse(course);
        presenter.prepareSuccessView(new UserOutputData(email, "teacher", user.getCourseNames()));

    }
}