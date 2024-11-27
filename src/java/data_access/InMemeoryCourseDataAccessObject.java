package data_access;

import entities.Course;

import java.util.HashMap;
import java.util.Map;

public class InMemeoryCourseDataAccessObject implements CourseDataAccessInterface {
    private final Map<String, Course> courses = new HashMap<>();
    @Override
    public boolean existsByCode(String courseCode) {
        return courses.containsKey(courseCode);
    }

    @Override
    public Course getCourseByCode(String courseCode) {
        return courses.get(courseCode);
    }
}
