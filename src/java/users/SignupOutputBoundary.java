package users;

public interface SignupOutputBoundary {
    void prepareSuccessView(UserInputData account);
    void prepareErrorView(String error);
}
