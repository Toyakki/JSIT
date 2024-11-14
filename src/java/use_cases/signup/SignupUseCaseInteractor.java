package use_cases.signup;

import data_access.InMemoryUserDataAccessObject;
import data_access.UserDataAccessInterface;
import entities.AccountFactory;
import entities.Account;
import use_cases.UserInputData;
import use_cases.UserOutputData;

public class SignupUseCaseInteractor implements SignupInputBoundary  {
    final SignupOutputBoundary userPresenter;
    final UserDataAccessInterface userDsGateway;


    public SignupUseCaseInteractor(InMemoryUserDataAccessObject userSignupDataAccessInterface,
                                   SignupOutputBoundary userPresenter
    ) {
        this.userPresenter = userPresenter;
        this.userDsGateway = userSignupDataAccessInterface;
    }

    public void createUser(UserInputData userInputData) {
        // signup logic
        if (userDsGateway.userExistsByEmail(userInputData.getEmail())) {
            userPresenter.prepareErrorView("User already exists.");
            return;
        }

        Account account = AccountFactory.createAccount(userInputData.getEmail(), userInputData.getPassword(),
                userInputData.getType());
        if (!account.passwordIsValid()) {
            userPresenter.prepareErrorView("Password is insufficient.");
            return;
        } else if (!account.emailIsValid()) {
            userPresenter.prepareErrorView("Email is invalid.");
            return;
        }

        userDsGateway.saveUser(account);

        UserOutputData userOutputData = new UserOutputData(userInputData.getEmail(), userInputData.getType());
        userPresenter.prepareSuccessView(userOutputData);
    }
}
