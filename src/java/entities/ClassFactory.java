package entities;

import java.util.ArrayList;

import java.util.UUID;

public class ClassFactory {
    public static Class createClass(String instructor, String className, ArrayList<String> students){
        return new Class(
                instructor,
                className,
                UUID.randomUUID().toString().replace("-", "").substring(0, 3),
                students
        );
    }
}
