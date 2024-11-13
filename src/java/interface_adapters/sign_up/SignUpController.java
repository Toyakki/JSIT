package interface_adapters.sign_up;

import users.UserInputData;
import users.signup.SignupInputBoundary;

public class SignUpController {
    private final SignupInputBoundary signUpUseCaseBoundary;

    public SignUpController(SignupInputBoundary signUpUseCaseBoundary) {
        this.signUpUseCaseBoundary = signUpUseCaseBoundary;
    }

    public void createUser(String email, String password, String type) {
        final UserInputData signUpData = new UserInputData(email, password, type);
        signUpUseCaseBoundary.createUser(signUpData);
    }
}
