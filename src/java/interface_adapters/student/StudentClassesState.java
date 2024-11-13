package interface_adapters.student;

import users.UserOutputData;

public class StudentClassesState {
    private String email = "";

    public StudentClassesState() { }

    public void setUser(UserOutputData user) {
        this.email = user.getEmail();
    }

    public String getEmail() {
        return this.email;
    }
}
