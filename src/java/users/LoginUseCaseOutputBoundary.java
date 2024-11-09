package users;

public interface LoginUseCaseOutputBoundary {
    void prepareFailView(String message);

    void prepareSuccessView(UserOutputData user);
}
