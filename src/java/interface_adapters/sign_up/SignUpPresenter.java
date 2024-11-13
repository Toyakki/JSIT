package interface_adapters.sign_up;

import interface_adapters.ViewManagerModel;
import interface_adapters.logged_in.LoggedInState;
import interface_adapters.logged_in.LoggedInViewModel;
import interface_adapters.login.LoginState;
import interface_adapters.login.LoginViewModel;
import users.UserOutputData;
import users.signup.SignupOutputBoundary;

public class SignUpPresenter implements SignupOutputBoundary {
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private ViewManagerModel viewManagerModel;

    public SignUpPresenter(LoginViewModel loginViewModel,
                           LoggedInViewModel loggedInViewModel,
                           ViewManagerModel viewManagerModel
    ) {
        this.loginViewModel = loginViewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    public void prepareErrorView(String error){
        final LoginState loginState = this.loginViewModel.getState();
        loginState.setLoginError(error);
        this.loginViewModel.setState(loginState);
        this.loginViewModel.firePropertyChange("error");
    }

    public void prepareSuccessView(users.UserOutputData userOutputData) {
        final LoggedInState loggedInState = this.loggedInViewModel.getState();
        loggedInState.setUser(userOutputData); // modified this
        this.loggedInViewModel.setState(loggedInState);
        this.loggedInViewModel.firePropertyChanged();

        this.viewManagerModel.setState(this.loggedInViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }
}
