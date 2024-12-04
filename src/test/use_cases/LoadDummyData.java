package use_cases;

import data_access.InMemoryUserDataAccessObject;
import data_access.UserDataAccessInterface;
import entities.Account;
import entities.Assignment;
import entities.Course;
import entities.CourseFactory;

import java.util.ArrayList;
import java.util.List;

public class LoadDummyData {

    public void loadData(UserDataAccessInterface demo) {
        Course csCourse = CourseFactory.createClass("Johnathon Calver", "CSC207");
        Course statsCourse = CourseFactory.createClass("Jeff Rosento", "STA257");
        Course matCourse = CourseFactory.createClass("Joe Repka", "MAT347");
        Course matCourseTwo = CourseFactory.createClass("Joe Repka", "MAT348");

        List<Course> joe_courses = new ArrayList<>();
        joe_courses.add(matCourse);
        joe_courses.add(matCourseTwo);

        List<Course> tohya_courses = new ArrayList<>();
        tohya_courses.add(statsCourse);
        tohya_courses.add(csCourse);

        List<Course> test_courses = new ArrayList<>();
        test_courses.add(statsCourse);
        test_courses.add(csCourse);
        test_courses.add(matCourse);


        Account joe = new Account("joerepka@mail.utoronto.ca", "algebra", "teacher", joe_courses);
        Account tohya = new Account("henrik-ibsen707@gmail.com", "thewildduck", "student", tohya_courses);
        Account sark = new Account("sark@gmail.com", "thewilduck1", "student", tohya_courses);
        Account test = new Account("t", "t", "teacher", test_courses);
        demo.saveUser(joe);
        demo.saveUser(tohya);
        demo.saveUser(test);
        demo.saveUser(sark);

        Assignment ps1 = new Assignment(matCourse,"Problem Set 1", "December 1st", "100", "graded", "true");
        Assignment ps2 = new Assignment(matCourse,"Problem Set 2", "December 15st", "100", "assigned", "false");
        Assignment ps3 = new Assignment(matCourse,"Problem Set 3", "December 15st", "100", "assigned", "false");
        Assignment ps4 = new Assignment(matCourse, "Problem Set 4", "December 15st", "100", "assigned", "false");
        Assignment ps5 = new Assignment(matCourse, "Problem Set 5", "December 15st", "100", "assigned", "false");
        Assignment ps6 = new Assignment(matCourse, "Problem Set 6", "December 15st", "100", "assigned", "false");
        Assignment ps7 = new Assignment(matCourse, "Problem Set 7", "December 15st", "100", "assigned", "false");
        Assignment finalProject = new Assignment(csCourse, "Final Project", "December 4th", "25", "submitted", "false");
        Assignment miniProject = new Assignment(csCourse, "Mini Project", "December 3rd", "10", "submitted", "false");

        csCourse.addAssignment(finalProject);
        csCourse.addAssignment(miniProject);
        matCourse.addAssignment(ps1);
        matCourse.addAssignment(ps2);
        matCourse.addAssignment(ps3);
        matCourse.addAssignment(ps4);
        matCourse.addAssignment(ps5);
        matCourse.addAssignment(ps6);
        matCourse.addAssignment(ps7);
    }
}
