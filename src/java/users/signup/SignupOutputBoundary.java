package users.signup;

import entities.Account;
import users.UserOutputData;

public interface SignupOutputBoundary {
    void prepareSuccessView(UserOutputData account);
    void prepareErrorView(String error);
}
