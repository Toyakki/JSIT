package use_cases.join_course;

import data_access.InMemoryUserDataAccessObject;
import entities.Account;
import entities.AccountFactory;
import entities.Course;
import entities.CourseFactory;
import interface_adapters.join_class.JoinCoursePresenter;
import interface_adapters.student.StudentClassesViewModel;
import interface_adapters.student_course.StudentCourseViewModel;
import org.junit.jupiter.api.Test;
import use_cases.UserOutputData;
import view.StudentCourseView;

import static org.junit.jupiter.api.Assertions.fail;

public class JoinCourseUseCaseInteractorTest {
    private InMemoryUserDataAccessObject userDsGateway;

    @Test
   void create(){
       userDsGateway = new InMemoryUserDataAccessObject();
       Account instructor = AccountFactory.createAccount("Lindsey@mail.com", "Abc123456!", "teacher");
       Account student = AccountFactory.createAccount("Enze@mail.com", "Abc123456!", "student");
       userDsGateway.saveUser(instructor);
       userDsGateway.saveUser(student);
       Course existedCourse = CourseFactory.createClass("Lindsey", "Software Design");
       instructor.addCourse(existedCourse);
   }

   public void testInvalidCourse(){
       StudentClassesViewModel studentClassesViewModel = new StudentClassesViewModel();
       JoinCoursePresenter presenter = new JoinCoursePresenter(studentClassesViewModel) {
           public void prepareFailView(String error) {assert true;}
           public void prepareSuccessView(UserOutputData account) {
               fail("Attempt to join a non-existed class, use case failure unexpected");
           }
       };
       JoinUseCaseInteractor interactor = new JoinUseCaseInteractor(presenter, userDsGateway );
       interactor.joinCourse("Enze@mail.com", "Analysis I");
   }


   public void testValidEnrollment(){
       JoinCoursePresenter presenter = new JoinCoursePresenter(){
           public void prepareFailView(String error) {
               fail("Enrolment valid, use case failure unexpected");
           }
           public void prepareSuccessView(UserOutputData account) {assert true;}
       };
       JoinUseCaseInteractor interactor = new JoinUseCaseInteractor(presenter, userDsGateway );
       interactor.joinCourse("Enze@mail.com", "Software Design", "Lindsey@mail.com");
   }



}
