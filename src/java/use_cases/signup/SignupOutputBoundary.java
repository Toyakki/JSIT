package use_cases.signup;

import use_cases.UserOutputData;

public interface SignupOutputBoundary {
    void prepareSuccessView(UserOutputData account);
    void prepareErrorView(String error);
}
