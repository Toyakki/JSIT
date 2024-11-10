package interface_adapters.logged_in;

public class LoggedInState {
    private String email = "";
    private String password = "";
    private String type;

    public LoggedInState(LoggedInState copy) {
        this.email = copy.email;
        this.password = copy.password;
        this.type = copy.type;
    }

    public LoggedInState(){

    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(String type){
        this.type = type;
    }
}
