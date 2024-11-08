package users;

import entities.AccountFactory;

public class LoginUseCaseInteractor implements LoginUseCaseBoundary {
    private final LoginUseCaseBoundary loginUseCaseBoundary;

    public LoginUseCaseInteractor(LoginUseCaseBoundary loginUseCaseBoundary) {
        this.loginUseCaseBoundary = loginUseCaseBoundary;
    }

    public void login(UserInputData userInputData) {
        // login logic
    }
}
