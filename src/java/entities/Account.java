package entities;

import java.util.List;

public class Account {
    private final String email;
    private final String password;
    private final String type;
    private List<String> courseNames;

    public Account(String email, String password, String type, List<String> courseNames) {
        this.email = email;
        this.password = password;
        this.type = type;
        this.courseNames = List.copyOf(courseNames);
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
        // Checks, in the following order, that the password contains at least one number, is at least 10 characters
        // long, has a lowercase letter, and has an uppercase letter.
        return !this.password.replaceAll("[^0-9]+", "").isBlank()
                && this.password.length() >= 10
                && !this.password.replaceAll("[^a-z]+", "").isBlank()
                && !this.password.replaceAll("[^A-Z]+", "").isBlank();
    }

    public boolean emailIsValid() {
        return this.email.length() >= 4 && this.email.contains("@");
    }

    public List<String> getCourseNames() {
        return List.copyOf(this.courseNames);
    }
}
