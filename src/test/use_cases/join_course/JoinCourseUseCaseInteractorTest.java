package use_cases.join_course;

import data_access.InMemoryUserDataAccessObject;
import entities.Account;
import entities.AccountFactory;
import entities.Course;
import entities.CourseFactory;
import interface_adapters.join_class.JoinCoursePresenter;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import use_cases.UserOutputData;

import static org.junit.jupiter.api.Assertions.fail;

public class JoinCourseUseCaseInteractorTest {
    private InMemoryUserDataAccessObject userDsGateway;

   @BeforeEach
    void create(){
       userDsGateway = new InMemoryUserDataAccessObject();
       Account instructor = AccountFactory.createAccount("Lindsey@mail.com", "Abc123456!", "teacher");
       Account student = AccountFactory.createAccount("Enze@mail.com", "Abc123456!", "student");
       userDsGateway.saveUser(instructor);
       userDsGateway.saveUser(student);
       Course existedCourse = CourseFactory.createClass("Lindsey", "Software Design");
       instructor.addCourse(existedCourse);
   }

   @Test
   public void testInvalidCourse(){
       JoinUseCaseOutputBoundary presenter = new JoinUseCaseOutputBoundary(){
           public void prepareFailView(String error) {assert true;}
           public void prepareSuccessView(UserOutputData account) {
               fail("Attempt to join a non-existed class, use case failure unexpected");
           }
       };
       JoinUseCaseInteractor interactor = new JoinUseCaseInteractor(presenter, userDsGateway );
       interactor.joinCourse("Enze@mail.com", "Analysis I");
   }

   @Test
   public void testInvalidInstructor(){
       JoinUseCaseOutputBoundary presenter = new JoinUseCaseOutputBoundary(){
           public void prepareFailView(String error) {assert true;}
           public void prepareSuccessView(UserOutputData account) {
               fail("Invalid instructor, use case failure unexpected");
           }
       };
       JoinUseCaseInteractor interactor = new JoinUseCaseInteractor(presenter, userDsGateway );
       interactor.joinCourse("Enze@mail.com", "Software Design");
   }

   @Test
   public void testValidEnrollment(){
       JoinUseCaseOutputBoundary presenter = new JoinUseCaseOutputBoundary(){
           public void prepareFailView(String error) {
               fail("Enrolment valid, use case failure unexpected");
           }
           public void prepareSuccessView(UserOutputData account) {assert true;}
       };
       JoinUseCaseInteractor interactor = new JoinUseCaseInteractor(presenter, userDsGateway );
       interactor.joinCourse("Enze@mail.com", "Software Design");
   }



}
