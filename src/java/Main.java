import com.formdev.flatlaf.FlatLightLaf;
import data_access.FileDataAccessInterface;
import data_access.InMemoryFileDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.UserDataAccessInterface;
import entities.*;
import interface_adapters.create_assignment.AssignmentCreaterController;
import interface_adapters.download.DownloadController;
import interface_adapters.grade.GradeController;
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
import use_cases.student_course_back.StudentCourseBackUseCase;
import use_cases.teacher_course_back.TeacherCourseBackUseCase;
import use_cases.login.LoginUseCaseInputBoundary;
import use_cases.login.LoginUseCaseInteractor;
import use_cases.login.LoginUseCaseOutputBoundary;
import use_cases.signup.SignupOutputBoundary;
import use_cases.signup.SignupInputBoundary;
import use_cases.signup.SignupUseCaseInteractor;
import use_cases.student_course_selection.StudentCourseViewInteractor;
import use_cases.teacher_course_selection.TeacherCourseViewInteractor;
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

        Assignment ps1 = new Assignment("Problem Set 1", "December 1st", "100", "graded", "true");
        Assignment ps2 = new Assignment("Problem Set 2", "December 15st", "100", "assigned", "false");
        Assignment finalProject = new Assignment("Final Project", "December 4th", "25", "submitted", "false");

        csCourse.addAssignment(finalProject);
        matCourse.addAssignment(ps1);
        matCourse.addAssignment(ps2);

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
        DownloadController downloadController = new DownloadController();
        StudentCourseBackController studentCourseBackController = new StudentCourseBackController(studentBackButtonInteractor);
        TeacherCourseBackController teacherCourseBackController = new TeacherCourseBackController(teacherCourseBackUseCase);
        AssignmentCreaterController assignmentCreaterController = new AssignmentCreaterController();
        GradeController gradeController = new GradeController();

        TeacherCourseViewPresenter teacherCourseViewPresenter = new TeacherCourseViewPresenter(teacherClassesViewModel,
                teacherCourseViewModel, viewManagerModel
        );
        TeacherCourseViewInteractor teacherCourseViewInteractor = new TeacherCourseViewInteractor(teacherCourseViewPresenter,
                fileDataAccessObject,
                demo
        );
        TeacherCourseViewController teacherCourseViewController = new TeacherCourseViewController(teacherCourseViewInteractor);

        // classes view for students
        studentClassesView = new StudentClassesView(studentClassesViewModel, studentCourseViewController);
        mainPanel.add(studentClassesView, studentClassesView.getViewName());

        // classes view for teachers
        teacherClassesView = new TeacherClassesView(teacherClassesViewModel, teacherCourseBackController, teacherCourseViewController);
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
        setSize(500, 425);
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
