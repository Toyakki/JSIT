package interface_adapters.login;

import use_cases.login.LoginUseCaseInputBoundary;
import use_cases.UserInputData;

public class LoginController {
    private final LoginUseCaseInputBoundary loginUseCaseInteractor;

    public LoginController(LoginUseCaseInputBoundary loginUseCaseInteractor) {
        this.loginUseCaseInteractor = loginUseCaseInteractor;
    }

    public void login(String email, String password) {
        final UserInputData loginInputData = new UserInputData(email, password);
        loginUseCaseInteractor.login(loginInputData);
    }
}
