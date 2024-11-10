import data_access.InMemoryUserDataAccessObject;
import entities.Account;
import interface_adapters.ViewManagerModel;
import interface_adapters.logged_in.LoggedInViewModel;
import interface_adapters.login.LoginController;
import interface_adapters.login.LoginPresenter;
import interface_adapters.login.LoginViewModel;
import users.LoginUseCaseInputBoundary;
import users.LoginUseCaseInteractor;
import users.LoginUseCaseOutputBoundary;
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

        // add login view to app
        loginView = new LoginView(loginViewModel, loginController);
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
        Main main = new Main();
        // will all move to attributes of the App Builder
//        CardLayout cardLayout = new CardLayout();
//        JPanel mainPanel = new JPanel(cardLayout);
//        ViewManagerModel viewManagerModel = new ViewManagerModel();
//        ViewManager viewManager = new ViewManager(mainPanel, cardLayout, viewManagerModel);
//
//        InMemoryUserDataAccessObject demo = new InMemoryUserDataAccessObject();

//        LoginView loginView;
//        LoginViewModel loginViewModel;
//        LoggedInViewModel loggedInViewModel;
//        TempLoggedInView loggedInView;
//        LoggedInViewModel loggedInViewModel;

        // load dummy data
//        Account sark = new Account("sark-asadourian@gmail.com", "password123", "student");
//        Account tohya = new Account("henrik-ibsen707@gmail.com", "thewildduck", "student");
//        Account isaac = new Account("isaac@gmail.com", "ftlopbd", "teacher");
//        Account jed = new Account("jedi@gmail.com", "jediiiiiiiiiii", "teacher");
//        Account test = new Account("t", "t", "teacher");
//        demo.save(sark);
//        demo.save(tohya);
//        demo.save(isaac);
//        demo.save(jed);
//        demo.save(test);
//
//        // add login view to app
//        LoginViewModel loginViewModel = new LoginViewModel();
//        LoginView loginView = new LoginView(loginViewModel);
//        mainPanel.add(loginView, loginView.getViewName());
//
//        // placeholder logged in screen
//        LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
//        TempLoggedInView loggedInView = new TempLoggedInView(loggedInViewModel);
//        mainPanel.add(loggedInView, loggedInView.getViewName());
//
//        // data access layer
//        LoginUseCaseOutputBoundary loginUseCaseOutputBoundary = new LoginPresenter(
//                loginViewModel,
//                loggedInViewModel,
//                viewManagerModel
//        );
//        LoginUseCaseInputBoundary loginInteractor = new LoginUseCaseInteractor(
//                demo,
//                loginUseCaseOutputBoundary
//        );
//        LoginController loginController = new LoginController(loginInteractor);
//        loginView.setLoginController(loginController);
//
//        // application layer
//        JFrame application = new JFrame("Login");
//        application.setSize(400, 200);
//        application.setContentPane(loginView.getPane());
//        application.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        application.add(mainPanel);
//
//        viewManagerModel.setState(loginView.getViewName());
//        viewManagerModel.firePropertyChanged();
//        application.setVisible(true);
    }
}
