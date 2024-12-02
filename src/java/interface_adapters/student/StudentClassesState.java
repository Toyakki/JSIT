package interface_adapters.student;

import use_cases.UserOutputData;

import java.util.ArrayList;
import java.util.List;

public class StudentClassesState {
    private String email = "";
    private List<String> courseNames;
    private String error = "";

    public StudentClassesState() {
        this.courseNames = new ArrayList<>();
    }

    public void setUser(UserOutputData user) {
        this.email = user.getEmail();
        this.courseNames = user.getCourseNames();
    }

    public String getEmail() {
        return this.email;
    }

    public List<String> getCourseNames(){
        return new ArrayList<>(this.courseNames);
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
