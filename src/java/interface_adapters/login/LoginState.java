package interface_adapters.login;

public class LoginState {
    private String email = "";
    private String password = "";
    private String type = "";
    private String loginError;

    public String getEmail() {
        return this.email;
    }

    public String getLoginError(){
        return this.loginError;
    }

    public String getPassword() {
        return this.password;
    }

    public String getType() {
        return this.type;
    }

    public void setLoginError(String loginError) {
        this.loginError = loginError;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
