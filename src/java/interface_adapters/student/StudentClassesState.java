package interface_adapters.student;

import use_cases.UserOutputData;

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
