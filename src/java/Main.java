import com.formdev.flatlaf.FlatLightLaf;
import data_access.FileDataAccessInterface;
import data_access.InMemoryFileDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.UserDataAccessInterface;
import entities.*;
import interface_adapters.ViewModel;
import interface_adapters.create_assignment.AssignmentCreaterController;
import interface_adapters.create_assignment.CreateAssignmentPresenter;
import interface_adapters.create_class.CreateCourseController;
import interface_adapters.create_class.CreateCoursePresenter;
import interface_adapters.download.DownloadController;
import interface_adapters.grade.GradeController;
import interface_adapters.grade.GradePresenter;
import interface_adapters.join_class.JoinCourseController;
import interface_adapters.join_class.JoinCoursePresenter;
import interface_adapters.student_course.StudentCourseBackController;
import interface_adapters.student_course.StudentCourseViewModel;
import interface_adapters.teacher_course.TeacherCourseBackController;
import interface_adapters.teacher_course.TeacherCourseViewModel;
import interface_adapters.ViewManagerModel;
import interface_adapters.login.LoginController;
import interface_adapters.login.LoginPresenter;
import interface_adapters.login.LoginViewModel;
import interface_adapters.sign_up.SignUpController;
import interface_adapters.sign_up.SignUpPresenter;
import interface_adapters.student.StudentClassesViewModel;
import interface_adapters.student.StudentCourseViewController;
import interface_adapters.student.StudentCourseViewPresenter;
import interface_adapters.student.StudentCoursesPresenter;
import interface_adapters.teacher.TeacherClassesViewModel;
import interface_adapters.teacher.TeacherCourseViewController;
import interface_adapters.teacher.TeacherCourseViewPresenter;
import interface_adapters.teacher.TeacherCoursesPresenter;
import use_cases.create_assignment.CreateAssignmentInteractor;
import use_cases.create_course.CreateCourseUseCaseInteractor;
import use_cases.download.DownloadInputBoundary;
import use_cases.download.DownloadInteractor;
import use_cases.grade.GradeInputBoundary;
import use_cases.grade.GradeInteractor;
import use_cases.grade.GradeOutputBoundary;
import use_cases.join_course.JoinUseCaseInteractor;
import use_cases.student_course_back.StudentCourseBackUseCase;
import use_cases.submit_grade.SubmitGradeInputBoundary;
import use_cases.submit_grade.SubmitGradeInteractor;
import use_cases.teacher_course_back.TeacherCourseBackUseCase;
import use_cases.login.LoginUseCaseInputBoundary;
import use_cases.login.LoginUseCaseInteractor;
import use_cases.login.LoginUseCaseOutputBoundary;
import use_cases.signup.SignupOutputBoundary;
import use_cases.signup.SignupInputBoundary;
import use_cases.signup.SignupUseCaseInteractor;
import use_cases.student_course_selection.StudentCourseViewInteractor;
import use_cases.teacher_course_selection.TeacherCourseViewInteractor;
import use_cases.upload_feedback.UploadFeedbackInputBoundary;
import use_cases.upload_feedback.UploadFeedbackInteractor;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    private final LoginViewModel loginViewModel = new LoginViewModel();
    private LoginView loginView;

    private final StudentClassesViewModel studentClassesViewModel = new StudentClassesViewModel();
    private final TeacherClassesViewModel teacherClassesViewModel = new TeacherClassesViewModel();

    private StudentClassesView studentClassesView;
    private TeacherClassesView teacherClassesView;

    private SignupInputBoundary signUpInteractor;
    private SignupOutputBoundary signUpPresenter;
    private SignUpController signUpController;

    private LoginUseCaseInputBoundary loginInteractor;
    private LoginController loginController;
    private LoginUseCaseOutputBoundary loginUseCaseOutputBoundary;

    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager;

    public Main() {
        // import look and feel for JSwing
        FlatLightLaf.setup();

        FileDataAccessInterface fileDataAccessObject = new InMemoryFileDataAccessObject();

        UserDataAccessInterface demo = new InMemoryUserDataAccessObject();

        // load dummy data
        Course csCourse = CourseFactory.createClass("Johnathon Calver", "CSC207");
        Course statsCourse = CourseFactory.createClass("Jeff Rosento", "STA257");
        Course matCourse = CourseFactory.createClass("Joe Repka", "MAT347");

        System.out.println(matCourse.getCourseCode());

        List<Course> joe_courses = new ArrayList<>();
        joe_courses.add(matCourse);

        List<Course> tohya_courses = new ArrayList<>();
        tohya_courses.add(statsCourse);
        tohya_courses.add(csCourse);

        List<Course> test_courses = new ArrayList<>();
        test_courses.add(statsCourse);
        test_courses.add(csCourse);
        test_courses.add(matCourse);

        Account joe = new Account("joerepka@mail.utoronto.ca", "algebra", "teacher", joe_courses);
        Account tohya = new Account("henrik-ibsen707@gmail.com", "thewildduck", "student", tohya_courses);
        Account test = new Account("t", "t", "teacher", test_courses);
        demo.saveUser(joe);
        demo.saveUser(tohya);
        demo.saveUser(test);

        Assignment ps1 = new Assignment(matCourse,"Problem Set 1", "December 1st", "100", "graded", "true");
        Assignment ps2 = new Assignment(matCourse,"Problem Set 2", "December 15st", "100", "assigned", "false");
        Assignment ps3 = new Assignment(matCourse,"Problem Set 3", "December 15st", "100", "assigned", "false");
        Assignment ps4 = new Assignment(matCourse, "Problem Set 4", "December 15st", "100", "assigned", "false");
        Assignment ps5 = new Assignment(matCourse, "Problem Set 5", "December 15st", "100", "assigned", "false");
        Assignment ps6 = new Assignment(matCourse, "Problem Set 6", "December 15st", "100", "assigned", "false");
        Assignment ps7 = new Assignment(matCourse, "Problem Set 7", "December 15st", "100", "assigned", "false");
        Assignment finalProject = new Assignment(csCourse, "Final Project", "December 4th", "25", "submitted", "false");

        csCourse.addAssignment(finalProject);
        matCourse.addAssignment(ps1);
        matCourse.addAssignment(ps2);
        matCourse.addAssignment(ps3);
        matCourse.addAssignment(ps4);
        matCourse.addAssignment(ps5);
        matCourse.addAssignment(ps6);
        matCourse.addAssignment(ps7);

        // data access layer
        loginUseCaseOutputBoundary = new LoginPresenter(
                loginViewModel,
                studentClassesViewModel,
                teacherClassesViewModel,
                viewManagerModel
        );

        loginInteractor = new LoginUseCaseInteractor(
                demo,
                loginUseCaseOutputBoundary
        );

        loginController = new LoginController(
                loginInteractor
        );

        signUpPresenter = new SignUpPresenter(loginViewModel,
                studentClassesViewModel, teacherClassesViewModel, viewManagerModel);

        signUpInteractor = new SignupUseCaseInteractor(
                demo,
                signUpPresenter
        );

        signUpController = new SignUpController(
                signUpInteractor
        );

        // add login view to app
        loginView = new LoginView(loginViewModel, loginController, signUpController);
        mainPanel.add(loginView, loginView.getViewName());

        // course view models
        TeacherCourseViewModel teacherCourseViewModel = new TeacherCourseViewModel();
        StudentCourseViewModel studentCourseViewModel = new StudentCourseViewModel();

        // course selection controllers and use case interactors and presenters
        StudentCourseViewPresenter studentCourseViewPresenter = new StudentCourseViewPresenter(
                studentClassesViewModel,
                studentCourseViewModel,
                viewManagerModel
        );
        StudentCourseViewInteractor studentCourseViewInteractor = new StudentCourseViewInteractor(
                studentCourseViewPresenter,
                fileDataAccessObject,
                demo
        );
        StudentCourseViewController studentCourseViewController = new StudentCourseViewController(
                studentCourseViewInteractor
        );

        // download, back, etc. controllers
        StudentCoursesPresenter studentCoursesPresenter = new StudentCoursesPresenter(viewManagerModel, studentClassesViewModel);
        StudentCourseBackUseCase studentBackButtonInteractor = new StudentCourseBackUseCase(demo, studentCoursesPresenter);
        TeacherCoursesPresenter teacherCoursesPresenter = new TeacherCoursesPresenter(viewManagerModel, teacherClassesViewModel);
        TeacherCourseBackUseCase teacherCourseBackUseCase = new TeacherCourseBackUseCase(demo, teacherCoursesPresenter);
        DownloadInputBoundary downloadInputBoundary = new DownloadInteractor(fileDataAccessObject, demo);
        DownloadController downloadController = new DownloadController(downloadInputBoundary);
        StudentCourseBackController studentCourseBackController = new StudentCourseBackController(studentBackButtonInteractor);
        TeacherCourseBackController teacherCourseBackController = new TeacherCourseBackController(teacherCourseBackUseCase);

        CreateAssignmentPresenter createAssignmentPresenter = new CreateAssignmentPresenter(teacherCourseViewModel);
        CreateAssignmentInteractor createAssignmentInteractor = new CreateAssignmentInteractor(
                createAssignmentPresenter,
                demo,
                fileDataAccessObject
        );
        AssignmentCreaterController assignmentCreaterController = new AssignmentCreaterController(createAssignmentInteractor);
        GradeOutputBoundary gradeOutputBoundary = new GradePresenter(teacherCourseViewModel);
        GradeInputBoundary gradeInputBoundary = new GradeInteractor(fileDataAccessObject, gradeOutputBoundary, demo);
        GradeController gradeController = new GradeController(gradeInputBoundary);

        TeacherCourseViewPresenter teacherCourseViewPresenter = new TeacherCourseViewPresenter(teacherClassesViewModel,
                teacherCourseViewModel, viewManagerModel
        );
        TeacherCourseViewInteractor teacherCourseViewInteractor = new TeacherCourseViewInteractor(teacherCourseViewPresenter,
                fileDataAccessObject,
                demo
        );
        TeacherCourseViewController teacherCourseViewController = new TeacherCourseViewController(teacherCourseViewInteractor);

        // create course controller, interactor, presenter
        CreateCoursePresenter createCoursePresenter = new CreateCoursePresenter(teacherClassesViewModel);
        CreateCourseUseCaseInteractor createCourseUseCaseInteractor = new CreateCourseUseCaseInteractor(createCoursePresenter, demo);
        CreateCourseController createCourseController = new CreateCourseController(createCourseUseCaseInteractor);

        // join course controller, interactor, presenter
        JoinCoursePresenter joinCoursePresenter = new JoinCoursePresenter(studentClassesViewModel);
        JoinUseCaseInteractor joinCourseUseCaseInteractor = new JoinUseCaseInteractor(joinCoursePresenter, demo);
        JoinCourseController joinCourseController = new JoinCourseController(joinCourseUseCaseInteractor);

        // classes view for students
        studentClassesView = new StudentClassesView(studentClassesViewModel, studentCourseViewController, joinCourseController);
        mainPanel.add(studentClassesView, studentClassesView.getViewName());

        // classes view for teachers
        teacherClassesView = new TeacherClassesView(teacherClassesViewModel, teacherCourseViewController, createCourseController);
        mainPanel.add(teacherClassesView, teacherClassesView.getViewName());

        // course view for student
        StudentCourseView studentCourseView = new StudentCourseView(
                studentCourseViewModel,
                studentCourseBackController,
                downloadController
        );
        mainPanel.add(studentCourseView, studentCourseView.getViewName());

        // course view for teacher
        TeacherCourseView teacherCourseView = new TeacherCourseView(
                teacherCourseViewModel,
                teacherCourseBackController,
                assignmentCreaterController,
                downloadController,
                gradeController
        );
        mainPanel.add(teacherCourseView, teacherCourseView.getViewName());

        add(mainPanel);
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(mainPanel);

        viewManager = new ViewManager(mainPanel, cardLayout, viewManagerModel);

        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChanged();
        setTitle("JSIT");
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
