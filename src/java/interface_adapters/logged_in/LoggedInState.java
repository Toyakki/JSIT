package interface_adapters.logged_in;

import users.UserOutputData;

public class LoggedInState {
    private String email = "";
    private String type = "";

    public LoggedInState() { }

    public void setUser(UserOutputData account) {
        this.email = account.getEmail();
        this.type = account.getType();
    }

    public String getType() {
        return this.type;
    }

    public String getEmail() {
        return this.email;
    }
}
