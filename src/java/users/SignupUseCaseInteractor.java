package users;

import entities.AccountFactory;
import entities.Account;
import data_access.UserSignupDataAccessInterface;

public class SignupUseCaseInteractor implements SignupUseCaseBoundary {
    final AccountFactory accountFactory;
    final SignupOutputBoundary userPresenter;
    final UserSignupDataAccessInterface userDsGateway;


    public SignupUseCaseInteractor(UserSignupDataAccessInterface userSignupDataAccessInterface,
                                   AccountFactory accountFactory, SignupOutputBoundary userPresenter) {
        this.userPresenter = userPresenter;
        this.accountFactory = accountFactory;
        this.userDsGateway = userSignupDataAccessInterface;
    }

    public void createUser(UserInputData userInputData) {
        // signup logic
        if (userDsGateway.existsByName(userInputData.getEmail())) {
            userPresenter.prepareErrorView("User already exists.");
        }

        Account account = AccountFactory.createAccount(userInputData.getEmail(), userInputData.getPassword(),
                userInputData.getType());
        if (!account.passwordIsValid()) {
            userPresenter.prepareErrorView("User password must have more than 5 characters.");
        }

        UserInputData user = new UserInputData(account.getEmail(), account.getPassword(), account.getType());
        userDsGateway.save(user);

        userPresenter.prepareSuccessView(user);
    }
}
