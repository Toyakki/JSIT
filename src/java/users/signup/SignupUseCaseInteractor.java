package users.signup;

import data_access.InMemoryUserDataAccessObject;
import entities.AccountFactory;
import entities.Account;
import users.UserInputData;
import users.UserOutputData;
import users.login.LoginUseCaseOutputBoundary;

public class SignupUseCaseInteractor implements SignupUseCaseBoundary {
//    final AccountFactory accountFactory;
    final LoginUseCaseOutputBoundary userPresenter;
    final InMemoryUserDataAccessObject userDsGateway;


    public SignupUseCaseInteractor(InMemoryUserDataAccessObject userSignupDataAccessInterface,
                                   LoginUseCaseOutputBoundary userPresenter) {
        this.userPresenter = userPresenter;
//        this.accountFactory = accountFactory;
        this.userDsGateway = userSignupDataAccessInterface;
    }

    public void createUser(UserInputData userInputData) {
        // signup logic
        if (userDsGateway.existsByEmail(userInputData.getEmail())) {
            userPresenter.prepareFailView("User already exists.");
            return;
        }

        Account account = AccountFactory.createAccount(userInputData.getEmail(), userInputData.getPassword(),
                userInputData.getType());
        if (!account.passwordIsValid()) {
            userPresenter.prepareFailView("Password is insufficient.");
            return;
        }

//        UserInputData user = new UserInputData(account.getEmail(), account.getPassword(), account.getType());
        userDsGateway.save(account);

        UserOutputData userOutputData = new UserOutputData(userInputData.getEmail(), userInputData.getPassword());
        userPresenter.prepareSuccessView(userOutputData);
    }
}
