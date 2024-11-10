import data_access.InMemoryUserDataAccessObject;
import entities.Account;
import entities.AccountFactory;
import interface_adapters.ViewManagerModel;
import interface_adapters.logged_in.LoggedInViewModel;
import interface_adapters.login.LoginController;
import interface_adapters.login.LoginPresenter;
import interface_adapters.login.LoginViewModel;
import interface_adapters.sign_up.SignUpController;
import users.login.LoginUseCaseInputBoundary;
import users.login.LoginUseCaseInteractor;
import users.login.LoginUseCaseOutputBoundary;
import users.signup.SignupOutputBoundary;
import users.signup.SignupUseCaseBoundary;
import users.signup.SignupUseCaseInteractor;
import view.LoginView;
import view.TempLoggedInView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    private final LoginViewModel loginViewModel = new LoginViewModel();
    private LoginView loginView;

    private final LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
    private TempLoggedInView loggedInView;

    private SignupUseCaseBoundary signUpInteractor;
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
        demo.save(sark);
        demo.save(tohya);
        demo.save(isaac);
        demo.save(jed);
        demo.save(test);

        // data access layer
        loginUseCaseOutputBoundary = new LoginPresenter(
                loginViewModel,
                loggedInViewModel,
                viewManagerModel
        );

        loginInteractor = new LoginUseCaseInteractor(
                demo,
                loginUseCaseOutputBoundary
        );

        loginController = new LoginController(
                loginInteractor
        );

        signUpInteractor = new SignupUseCaseInteractor(
                demo,
                loginUseCaseOutputBoundary
        );

        signUpController = new SignUpController(
                signUpInteractor
        );

        // add login view to app
        loginView = new LoginView(loginViewModel, loginController, signUpController);
        mainPanel.add(loginView, loginView.getViewName());

        // placeholder logged in screen
        loggedInView = new TempLoggedInView(loggedInViewModel);
        mainPanel.add(loggedInView, loggedInView.getViewName());

        add(mainPanel);
        setSize(400, 200);
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
