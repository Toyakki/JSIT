package users.signup;

import data_access.InMemoryUserDataAccessObject;
import entities.AccountFactory;
import entities.Account;
import users.UserInputData;

public class SignupUseCaseInteractor implements SignupUseCaseBoundary {
    final AccountFactory accountFactory;
    final SignupOutputBoundary userPresenter;
    final InMemoryUserDataAccessObject userDsGateway;


    public SignupUseCaseInteractor(InMemoryUserDataAccessObject userSignupDataAccessInterface,
                                   AccountFactory accountFactory, SignupOutputBoundary userPresenter) {
        this.userPresenter = userPresenter;
        this.accountFactory = accountFactory;
        this.userDsGateway = userSignupDataAccessInterface;
    }

    public void createUser(UserInputData userInputData) {
        // signup logic
        if (userDsGateway.existsByEmail(userInputData.getEmail())) {
            userPresenter.prepareErrorView("User already exists.");
            return;
        }

        Account account = AccountFactory.createAccount(userInputData.getEmail(), userInputData.getPassword(),
                userInputData.getType());
        if (!account.passwordIsValid()) {
            userPresenter.prepareErrorView("Password is insufficient.");
            return;
        }

//        UserInputData user = new UserInputData(account.getEmail(), account.getPassword(), account.getType());
        userDsGateway.save(account);

        userPresenter.prepareSuccessView(userInputData);
    }
}