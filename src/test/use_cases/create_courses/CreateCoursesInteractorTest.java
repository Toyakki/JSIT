package use_cases.create_courses;

import data_access.InMemoryUserDataAccessObject;
import entities.Account;
import entities.AccountFactory;
import entities.Course;
import entities.CourseFactory;

import java.util.List;

public class CreateCoursesInteractorTest {
    private InMemoryUserDataAccessObject userDsGateway;

    void create(){
        userDsGateway = new InMemoryUserDataAccessObject();
        Account instructor = AccountFactory.createAccount("lindsey@mail.com", "Abc123456!",
                "teacher");
        userDsGateway.saveUser(instructor);
        Course existedCourse = CourseFactory.createClass("Lindsey", "Software Design");
        instructor.addCourse(existedCourse);
    }

    public void testExistedCourse() {
        Account teacher = this.userDsGateway.getUserByEmail("lindsey@mail.com");
        List<Course> courses = teacher.getCourses();
        Course course = courses.getFirst();
        assertTrue(courses.size() == 1);
        assertEqual("Software Design", course.getName());
        assertEqual("Lindsey", course.getInstructor());
    }

    public void testNewCourse(Course course) {
        Account teacher = this.userDsGateway.getUserByEmail("lindsey@mail.com");
        Course newCourse = CourseFactory.createClass("Lindsey", "Analysis I");
        teacher.addCourse(newCourse);
        List<Course> courses = teacher.getCourses();
        assertTrue(courses.size() == 2);
        Course foundCourse = teacher.getCourseByName("Analysis I");
        assertEqual(foundCourse.getInstructor(), newCourse.getInstructor());
        assertEqual(foundCourse.getName(), newCourse.getName());
    }
}

