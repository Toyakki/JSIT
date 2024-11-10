package interface_adapters.login;

import interface_adapters.ViewModel;

public class LoginViewModel extends ViewModel<LoginState> {
    public LoginViewModel() {
        super("log in");
        setState(new LoginState());
    }
}
