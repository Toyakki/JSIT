package interface_adapters.teacher;

import users.UserOutputData;

public class TeacherClassesState {
    private String email = "";

    public TeacherClassesState() { }

    public void setUser(UserOutputData user) {
        this.email = user.getEmail();
    }

    public String getEmail() {
        return this.email;
    }
}
