package interface_adapters.logged_in;

import interface_adapters.ViewModel;

public class LoggedInViewModel extends ViewModel<LoggedInState> {
    public LoggedInViewModel() {
        super("student classes view");
        setState(new LoggedInState());
    }
}
