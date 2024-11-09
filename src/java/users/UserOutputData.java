package users;

public class UserOutputData {
    private String email;
    private String type;

    public UserOutputData(String email, String type) {
        this.email = email;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }
}
