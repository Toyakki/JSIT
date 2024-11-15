package use_cases;

import java.util.ArrayList;
import java.util.List;

public class UserOutputData {
    private String email;
    private String type;
    private List<String> courseNames;

    public UserOutputData(String email, String type, List<String> courseNames) {
        this.email = email;
        this.type = type;
        this.courseNames = new ArrayList<>(courseNames);
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public List<String> getCourseNames() {
        return new ArrayList<>(this.courseNames);
    }
}
