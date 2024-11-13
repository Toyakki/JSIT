package users;

public class UserInputData {

    final private String email;
    final private String password;
    final private String type;

    public UserInputData(String email, String password, String type) {
        this.email = email;
        this.type= type;
        this.password = password;
    }

    public UserInputData(String email, String password) {
        this.email = email;
        this.password = password;
        this.type = "";
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }


}