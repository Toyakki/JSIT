package use_cases.signup;

import data_access.InMemoryUserDataAccessObject;
import entities.Account;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import use_cases.UserInputData;
import use_cases.UserOutputData;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SignupUseCaseInteractorTest {
    private InMemoryUserDataAccessObject userDsGateway;

    @BeforeEach
    void create(){

        userDsGateway = new InMemoryUserDataAccessObject();
        Account existedUser = new Account("enze@gmail.com", "Abc123456!", "student", new ArrayList<>());
        userDsGateway.saveUser(existedUser);
    }

    @Test
    public void testValidAccount() {
        SignupOutputBoundary userPresenter = new SignupOutputBoundary() {
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

    @Test
    public void testInvalidPassword() {
        SignupOutputBoundary userPresenter = new SignupOutputBoundary() {

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


    @Test
    public void testInvalidEmail() {
        SignupOutputBoundary userPresenter = new SignupOutputBoundary() {
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

    @Test
    public void testExistedUser() {
        SignupOutputBoundary userPresenter = new SignupOutputBoundary() {
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
