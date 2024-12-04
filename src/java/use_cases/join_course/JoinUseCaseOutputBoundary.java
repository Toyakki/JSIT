package use_cases.join_course;

import use_cases.UserOutputData;

public interface JoinUseCaseOutputBoundary {
    void prepareFailView(String error);
    void prepareSuccessView(UserOutputData user);
}
