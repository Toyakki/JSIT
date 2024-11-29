package entities;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private final String email;
    private final String password;
    private final String type;
    private List<Course> courses;
    private List<String> courseNames;

    public Account(String email, String password, String type, List<Course> courses) {
        this.email = email;
        this.password = password;
        this.type = type;
        this.courses = courses;
        this.courseNames = new ArrayList<>();
        for (Course course : this.courses) {
            this.courseNames.add(course.getName());
            if (this.type.equals("student")){
                course.addStudent(email);
            } else {
                course.setInstructor(email);
            }

        }
    }

    public Course getCourseByName(String name) {
        Course course = null;
        for (Course candidateCourse : this.courses){
            if (candidateCourse.getName().equals(name)){
                course = candidateCourse;
                break;
            }
        }
        if (course == null){
            throw new IllegalArgumentException("Course not found");
        }
        return course;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
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

    public List<Course> getCourses() {
        return List.copyOf(this.courses);
    }
}
