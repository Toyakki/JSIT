package use_cases.signup;

import use_cases.UserInputData;

public interface SignupInputBoundary {
    void createUser(UserInputData userInputData);
}
