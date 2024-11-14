package entities;

import java.util.ArrayList;

import java.util.UUID;

public class CourseFactory {
    public static Course createClass(String instructor, String className, ArrayList<String> students){
        return new Course(
                instructor,
                className,
                UUID.randomUUID().toString().replace("-", "").substring(0, 3),
                students
        );
    }
}
