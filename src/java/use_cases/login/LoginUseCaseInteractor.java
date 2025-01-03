package use_cases.login;

import data_access.InMemoryUserDataAccessObject;
import data_access.UserDataAccessInterface;
import use_cases.UserInputData;
import use_cases.UserOutputData;

public class LoginUseCaseInteractor implements LoginUseCaseInputBoundary {
    private final UserDataAccessInterface userDsGateway;
    private final LoginUseCaseOutputBoundary userPresenter;

    public LoginUseCaseInteractor(UserDataAccessInterface userDsGateway,
                                  LoginUseCaseOutputBoundary loginUseCaseBoundary) {
        this.userDsGateway = userDsGateway;
        this.userPresenter = loginUseCaseBoundary;
    }

    public void login(UserInputData userInputData) {
        if (!this.userDsGateway.userExistsByEmail(userInputData.getEmail())) {
            this.userPresenter.prepareFailView("User does not exist.");
            return;
        } else if (!this.userDsGateway
                .getUserByEmail(userInputData.getEmail())
                .getPassword().equals(userInputData.getPassword())) {
            this.userPresenter.prepareFailView("Incorrect email or password.");
            return;
        }

        UserOutputData user = new UserOutputData(userInputData.getEmail(),
                this.userDsGateway.getUserByEmail(userInputData.getEmail()).getType(),
                this.userDsGateway.getUserByEmail(userInputData.getEmail()).getCourseNames()
        );
        this.userPresenter.prepareSuccessView(user);
    }
}
