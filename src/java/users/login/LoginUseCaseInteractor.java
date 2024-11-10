package users.login;

import data_access.InMemoryUserDataAccessObject;
import users.UserInputData;
import users.UserOutputData;

public class LoginUseCaseInteractor implements LoginUseCaseInputBoundary {
    private final InMemoryUserDataAccessObject userDsGateway;
    private final LoginUseCaseOutputBoundary userPresenter;

    public LoginUseCaseInteractor(InMemoryUserDataAccessObject userDsGateway,
                                  LoginUseCaseOutputBoundary loginUseCaseBoundary) {
        this.userDsGateway = userDsGateway;
        this.userPresenter = loginUseCaseBoundary;
    }

    public void login(UserInputData userInputData) {
        if (!this.userDsGateway.existsByEmail(userInputData.getEmail())) {
            this.userPresenter.prepareFailView("User does not exist.");
            return;
        } else if (!this.userDsGateway
                .getByEmail(userInputData.getEmail())
                .getPassword().equals(userInputData.getPassword())) {
            this.userPresenter.prepareFailView("Incorrect email or password.");
            return;
        }

        UserOutputData user = new UserOutputData(userInputData.getEmail(), userInputData.getType());
        this.userPresenter.prepareSuccessView(user);
    }
}
