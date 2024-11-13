package interface_adapters.sign_up;

import interface_adapters.ViewManagerModel;
import interface_adapters.logged_in.LoggedInState;
import interface_adapters.logged_in.LoggedInViewModel;
import interface_adapters.login.LoginState;
import interface_adapters.login.LoginViewModel;
import interface_adapters.student.StudentClassesState;
import interface_adapters.student.StudentClassesViewModel;
import interface_adapters.teacher.TeacherClassesState;
import interface_adapters.teacher.TeacherClassesViewModel;
import users.UserOutputData;
import users.signup.SignupOutputBoundary;

public class SignUpPresenter implements SignupOutputBoundary {
    private LoginViewModel loginViewModel;
    private StudentClassesViewModel studentClassesViewModel;
    private TeacherClassesViewModel teacherClassesViewModel;
    private ViewManagerModel viewManagerModel;

    public SignUpPresenter(LoginViewModel loginViewModel,
                           StudentClassesViewModel studentClassesViewModel,
                           TeacherClassesViewModel teacherClassesViewModel,
                           ViewManagerModel viewManagerModel
    ) {
        this.loginViewModel = loginViewModel;
        this.studentClassesViewModel = studentClassesViewModel;
        this.teacherClassesViewModel = teacherClassesViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    public void prepareErrorView(String error){
        final LoginState loginState = this.loginViewModel.getState();
        loginState.setLoginError(error);
        this.loginViewModel.setState(loginState);
        this.loginViewModel.firePropertyChange("error");
    }

    public void prepareSuccessView(UserOutputData userOutputData) {
        // load teacher vs student view model depending on type of output data
        if (userOutputData.getType().equals("student")){
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
}
