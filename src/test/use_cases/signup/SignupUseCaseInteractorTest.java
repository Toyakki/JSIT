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


import static org.junit.jupiter.api.Assertions.*;

class SignupUseCaseInteractorTest {
    private InMemoryUserDataAccessObject userDsGateway;

    void create(){
        LoginViewModel loginViewModel = new LoginViewModel();
        StudentClassesViewModel studentClassesViewModel = new StudentClassesViewModel();
        TeacherClassesViewModel teacherClassesViewModel = new TeacherClassesViewModel();
        ViewManagerModel viewManagerModel = new ViewManagerModel();
//        SignupOutputBoundary userPresenter = new SignUpPresenter(loginViewModel, studentClassesViewModel,
//                teacherClassesViewModel, viewManagerModel);
        userDsGateway = new InMemoryUserDataAccessObject();
//        SignupUseCaseInteractor interactor = new SignupUseCaseInteractor(userDsGateway, userPresenter);
        userDsGateway.saveUser(new Account("enze@gmail.com", "Abc123456!", "student"));
    }


    public void testValidAccount(UserOutputData account) {
        SignupOutputBoundary userPresenter = new SignUpPresenter() {
            @Override
            public void prepareErrorView(String error){
                fail("Valid user");
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
        interactor.createUser(new UserInputData(
                "@gmail.com", "Abc123456!", "student"
        ));
    }

}