package use_cases.signup;

import data_access.InMemoryUserDataAccessObject;
import data_access.UserDataAccessInterface;
import entities.Account;
import interface_adapters.ViewManagerModel;
import interface_adapters.login.LoginViewModel;
import interface_adapters.sign_up.SignUpPresenter;
import interface_adapters.student.StudentClassesViewModel;
import interface_adapters.teacher.TeacherClassesViewModel;
import use_cases.UserInputData;
import use_cases.UserOutputData;
import view.TeacherClassesView;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SignupUseCaseInteractorTest {
    private InMemoryUserDataAccessObject userDsGateway;

    void create(){
//        LoginViewModel loginViewModel = new LoginViewModel();
//        StudentClassesViewModel studentClassesViewModel = new StudentClassesViewModel();
//        TeacherClassesViewModel teacherClassesViewModel = new TeacherClassesViewModel();
//        ViewManagerModel viewManagerModel = new ViewManagerModel();
//        SignupOutputBoundary userPresenter = new SignUpPresenter(loginViewModel, studentClassesViewModel,
//                teacherClassesViewModel, viewManagerModel);
//        SignupUseCaseInteractor interactor = new SignupUseCaseInteractor(userDsGateway, userPresenter);
        userDsGateway = new InMemoryUserDataAccessObject();
        Account existedUser = new Account("enze@gmail.com", "Abc123456!", "student", new ArrayList<>());
        userDsGateway.saveUser(existedUser);
    }


    public void testValidAccount(UserOutputData account) {
        SignupOutputBoundary userPresenter = new SignUpPresenter() {
            @Override
            public void prepareErrorView(String error){
                fail("Valid user, use case failure is unexpected.");
            }

            @Override
            public void prepareSuccessView(UserOutputData account) {
                assertEquals("@gmail.com", account.getEmail());
                assertEquals("student", account.getType());
                assertTrue(userDsGateway.userExistsByEmail("@gmail.com"));
                Account userstored = userDsGateway.getUserByEmail("@gmail.com");
                assertEquals("@gmail.com", userstored.getEmail());
                assertEquals("student", userstored.getType());

            }
        };
        SignupInputBoundary interactor = new SignupUseCaseInteractor(userDsGateway, userPresenter);
        UserInputData validUser = new UserInputData("@gmail.com", "Abc123456!", "student");
        interactor.createUser(validUser);
    }

    public void testInvalidPassword(UserOutputData account) {
        SignupOutputBoundary userPresenter = new SignUpPresenter() {
            @Override
            public void prepareErrorView(String error){
                assert true;
            }

            @Override
            public void prepareSuccessView(UserOutputData account) {
                fail("User with invalid password, use case failure is unexpected");
            }
        };
        SignupInputBoundary interactor = new SignupUseCaseInteractor(userDsGateway, userPresenter);
        UserInputData invalidPasswordUser = new UserInputData("@gmail.com", "aa", "student");
        interactor.createUser(invalidPasswordUser);
    }

    public void testInvalidEmail(UserOutputData account) {
        SignupOutputBoundary userPresenter = new SignUpPresenter() {
            @Override
            public void prepareErrorView(String error){
                assert true;
            }

            @Override
            public void prepareSuccessView(UserOutputData account) {
                fail("User with invalid username, use case failure is unexpected");
            }
        };
        SignupInputBoundary interactor = new SignupUseCaseInteractor(userDsGateway, userPresenter);
        UserInputData invalidEmailUser = new UserInputData("@om", "aAbc123456!", "student");
        interactor.createUser(invalidEmailUser);
    }

    public void testExistedUser(UserOutputData account) {
        SignupOutputBoundary userPresenter = new SignUpPresenter() {
            @Override
            public void prepareErrorView(String error){
                assert true;
            }

            @Override
            public void prepareSuccessView(UserOutputData account) {
                fail("User already existed, use case failure is unexpected");
            }
        };
        SignupInputBoundary interactor = new SignupUseCaseInteractor(userDsGateway, userPresenter);
        UserInputData existedUser = new UserInputData("enze@gmail.com", "Abc123456!", "student");
        interactor.createUser(existedUser);
    }
}