package users.signup;

import users.UserInputData;

public interface SignupOutputBoundary {
    void prepareSuccessView(UserInputData account);
    void prepareErrorView(String error);
}
