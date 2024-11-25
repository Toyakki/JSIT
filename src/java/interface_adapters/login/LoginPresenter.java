package interface_adapters.login;

import interface_adapters.ViewManagerModel;
import interface_adapters.student.StudentClassesState;
import interface_adapters.student.StudentClassesViewModel;
import interface_adapters.teacher.TeacherClassesState;
import interface_adapters.teacher.TeacherClassesViewModel;
import use_cases.login.LoginUseCaseOutputBoundary;
import use_cases.UserOutputData;

public class LoginPresenter implements LoginUseCaseOutputBoundary {
    private final LoginViewModel loginViewModel;
    private final StudentClassesViewModel studentClassesViewModel;
    private final TeacherClassesViewModel teacherClassesViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoginPresenter(LoginViewModel loginViewModel,
                          StudentClassesViewModel studentLoggedInViewModel,
                          TeacherClassesViewModel teacherLoggedInViewModel,
                          ViewManagerModel viewManagerModel) {
        this.loginViewModel = loginViewModel;
        this.studentClassesViewModel = studentLoggedInViewModel;
        this.teacherClassesViewModel = teacherLoggedInViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    public void prepareSuccessView(UserOutputData userOutputData){
        // load teacher vs student view model depending on type of output data
        if (userOutputData.getType().equals("student")){
            // get current state
            final StudentClassesState studentClassesState = studentClassesViewModel.getState();

            studentClassesState.setUser(userOutputData);
            this.studentClassesViewModel.setState(studentClassesState);
            this.studentClassesViewModel.firePropertyChanged();

            this.viewManagerModel.setState(studentClassesViewModel.getViewName());
            this.viewManagerModel.firePropertyChanged();
        } else if (userOutputData.getType().equals("teacher")){
            final TeacherClassesState teacherClassesState = teacherClassesViewModel.getState();
            teacherClassesState.setUser(userOutputData);
            this.teacherClassesViewModel.setState(teacherClassesState);
            this.teacherClassesViewModel.firePropertyChanged();

            this.viewManagerModel.setState(teacherClassesViewModel.getViewName());
            this.viewManagerModel.firePropertyChanged();
        }
    }

    public void prepareFailView(String error){
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.firePropertyChanged();
        loginViewModel.firePropertyChange("error");
    }
}
