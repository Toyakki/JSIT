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
        return !this.password.replaceAll("[^0,9]", this.password).isBlank() && this.password.length() >= 6;
    }

    public boolean emailIsValid() {
        return this.email.length() >= 4 && this.email.contains("@");
    }
}
