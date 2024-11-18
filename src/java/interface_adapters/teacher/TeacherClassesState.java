package interface_adapters.teacher;

import use_cases.UserOutputData;

import java.util.ArrayList;
import java.util.List;

public class TeacherClassesState {
    private String email = "";
    private List<String> courseNames;

    public TeacherClassesState() { }

    public void setUser(UserOutputData user) {
        this.email = user.getEmail();
        this.courseNames = new ArrayList<>(user.getCourseNames());
    }

    public String getEmail() {
        return this.email;
    }

    public List<String> getCourseNames() {
        return new ArrayList<>(this.courseNames);
    }
}
