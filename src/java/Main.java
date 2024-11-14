import data_access.InMemoryUserDataAccessObject;
import entities.Account;
import interface_adapters.ViewManagerModel;
import interface_adapters.login.LoginController;
import interface_adapters.login.LoginPresenter;
import interface_adapters.login.LoginViewModel;
import interface_adapters.sign_up.SignUpController;
import interface_adapters.sign_up.SignUpPresenter;
import interface_adapters.student.StudentClassesViewModel;
import interface_adapters.teacher.TeacherClassesViewModel;
import users.login.LoginUseCaseInputBoundary;
import users.login.LoginUseCaseInteractor;
import users.login.LoginUseCaseOutputBoundary;
import users.signup.SignupOutputBoundary;
import users.signup.SignupInputBoundary;
import users.signup.SignupUseCaseInteractor;
import view.LoginView;
import view.StudentClassesView;
import view.TeacherClassesView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

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
    private SignupOutputBoundary signUpUseCaseOutputBoundary;
    private SignUpController signUpController;

    private LoginUseCaseInputBoundary loginInteractor;
    private LoginController loginController;
    private LoginUseCaseOutputBoundary loginUseCaseOutputBoundary;

    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager;

    public Main(){
        InMemoryUserDataAccessObject demo = new InMemoryUserDataAccessObject();

        // load dummy data
        Account sark = new Account("sark-asadourian@gmail.com", "password123", "student");
        Account tohya = new Account("henrik-ibsen707@gmail.com", "thewildduck", "student");
        Account isaac = new Account("isaac@gmail.com", "ftlopbd", "teacher");
        Account jed = new Account("jedi@gmail.com", "jediiiiiiiiiii", "teacher");
        Account test = new Account("t", "t", "teacher");
        demo.saveUser(sark);
        demo.saveUser(tohya);
        demo.saveUser(isaac);
        demo.saveUser(jed);
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

        SignupOutputBoundary signUpPresenter = new SignUpPresenter(loginViewModel,
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

        // classes view for students
        studentClassesView = new StudentClassesView(studentClassesViewModel);
        mainPanel.add(studentClassesView, studentClassesView.getViewName());

        // classes view for teachers
        teacherClassesView = new TeacherClassesView(teacherClassesViewModel);
        mainPanel.add(teacherClassesView, teacherClassesView.getViewName());

        add(mainPanel);
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(mainPanel);

        viewManager = new ViewManager(mainPanel, cardLayout, viewManagerModel);

        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChanged();
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
