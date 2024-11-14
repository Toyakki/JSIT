package use_cases.login;

import use_cases.UserOutputData;

public interface LoginUseCaseOutputBoundary {
    void prepareFailView(String message);

    void prepareSuccessView(UserOutputData user);
}
