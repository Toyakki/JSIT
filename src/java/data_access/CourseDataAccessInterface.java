package data_access;

import entities.Course;

public interface CourseDataAccessInterface {
    boolean existsByCode(String courseCode);

    Course getCourseByCode(String courseCode);
}
