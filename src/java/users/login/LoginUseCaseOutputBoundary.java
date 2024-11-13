package users.login;

import users.UserInputData;
import users.UserOutputData;

public interface LoginUseCaseOutputBoundary {
    void prepareFailView(String message);

    void prepareSuccessView(UserOutputData user);
}
