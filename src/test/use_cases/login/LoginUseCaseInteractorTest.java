package use_cases.login;

import data_access.InMemoryUserDataAccessObject;
import entities.Account;
import interface_adapters.login.LoginPresenter;
import use_cases.UserInputData;
import use_cases.UserOutputData;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LoginUseCaseInteractorTest {
    private InMemoryUserDataAccessObject userDsGateway;

    void create() {
        userDsGateway = new InMemoryUserDataAccessObject();
        Account existedUser = new Account("enze@gmail.com", "Abc123456!", "student", new ArrayList<>());
        userDsGateway.saveUser(existedUser);
    }

    public void testValidLogin(UserOutputData account) {
        LoginUseCaseOutputBoundary userPresenter = new LoginPresenter() {
            @Override
            public void prepareFailView(String error){
                fail("Login should be successful, use case failure is unexpected.");
            }

            @Override
            public void prepareSuccessView(UserOutputData account) {
                assert true;

            }
        };
        LoginUseCaseInputBoundary interactor = new LoginUseCaseInteractor(userDsGateway, userPresenter);
        UserInputData validLogin = new UserInputData("enze@gmail.com", "Abc123456!", "student");
        interactor.login(validLogin);
    }

    public void testLoginWithWrongPassword(UserOutputData account) {
        LoginUseCaseOutputBoundary userPresenter = new LoginPresenter() {
            @Override
            public void prepareFailView(String error){
                assert true;
            }

            @Override
            public void prepareSuccessView(UserOutputData account) {
                fail("Attempt to login with wrong password, use case failure is unexpected.");

            }
        };
        LoginUseCaseInputBoundary interactor = new LoginUseCaseInteractor(userDsGateway, userPresenter);
        UserInputData wrongPasswordLogin = new UserInputData("enze@gmail.com", "Ab12345!", "student");
        interactor.login(wrongPasswordLogin);
    }

    public void testLoginWithWrongEmail(UserOutputData account) {
        LoginUseCaseOutputBoundary userPresenter = new LoginPresenter() {
            @Override
            public void prepareFailView(String error){
                assert true;
            }

            @Override
            public void prepareSuccessView(UserOutputData account) {
                fail("Attempt to login with invalid email, use case failure is unexpected.");

            }
        };
        LoginUseCaseInputBoundary interactor = new LoginUseCaseInteractor(userDsGateway, userPresenter);
        UserInputData wrongEmailLogin = new UserInputData("enze@mail.com", "Abc123456!", "student");
        interactor.login(wrongEmailLogin);
    }
}