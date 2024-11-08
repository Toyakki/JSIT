package users;

public class UserInputData {

    final private String email;
    final private String password;
    final private String type;

    public UserSignupInputData(String email, String password, String type) {
        this.email = email;
        this.type= type;
        this.password = password;
    }

    String getName() {
        return email;
    }

    String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }
}