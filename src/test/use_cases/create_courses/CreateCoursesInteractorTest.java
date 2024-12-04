package use_cases.create_courses;

import data_access.InMemoryUserDataAccessObject;
import entities.Account;
import entities.AccountFactory;
import entities.Course;
import entities.CourseFactory;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CreateCoursesInteractorTest {
    private InMemoryUserDataAccessObject userDsGateway;

    @BeforeEach
    void create(){
        userDsGateway = new InMemoryUserDataAccessObject();
        Account instructor = AccountFactory.createAccount("lindsey@mail.com", "Abc123456!",
                "teacher");
        userDsGateway.saveUser(instructor);
        Course existedCourse = CourseFactory.createClass("Lindsey", "Software Design");
        instructor.addCourse(existedCourse);
    }

    @Test
    public void testExistedCourse() {
        Account teacher = this.userDsGateway.getUserByEmail("lindsey@mail.com");
        List<Course> courses = teacher.getCourses();
        Course course = courses.getFirst();
        assertEquals(1, courses.size());
        assertEquals("Software Design", course.getName());
        assertEquals("Lindsey", course.getInstructor());
    }

    @Test
    public void testNewCourse() {
        Account teacher = this.userDsGateway.getUserByEmail("lindsey@mail.com");
        Course newCourse = CourseFactory.createClass("Lindsey", "Analysis I");
        teacher.addCourse(newCourse);
        List<Course> courses = teacher.getCourses();
        assertEquals(2, courses.size());
        Course foundCourse = teacher.getCourseByName("Analysis I");
        assertEquals(foundCourse.getInstructor(), newCourse.getInstructor());
        assertEquals(foundCourse.getName(), newCourse.getName());
    }
}

