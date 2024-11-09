package entities;

public class Account {
    private final String email;
    private final String password;
    private final String type;

    public Account(String email, String password, String type) {
        this.email = email;
        this.password = password;
        this.type = type;
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

    public boolean passwordIsValid() {
        return this.password.length() >= 16
                && this.password.contains("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
                && this.password.contains("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase())
                && this.password.contains("1234567890");
    }
}
