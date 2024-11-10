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

public class Main {
//    private final JPanel cardPanel = new JPanel();
//    private final CardLayout cardLayout = new CardLayout();
//    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
//    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
//
//    private final InMemoryUserDataAccessObject userDataAccessObject = new InMemoryUserDataAccessObject();
//
//    private LoginView loginView;
//    private LoginViewModel loginViewModel;
//    private LoggedInViewModel loggedInViewModel;
//    private TempLoggedInView loggedInView;
//    private LoggedInViewModel loggedInViewModel;

    public static void main(String[] args) {
        // will all move to attributes of the App Builder
        Container cardPanels = new JPanel();
        CardLayout cardLayout = new CardLayout();
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        ViewManager viewManager = new ViewManager(cardPanels, cardLayout, viewManagerModel);

        InMemoryUserDataAccessObject demo = new InMemoryUserDataAccessObject();

//        LoginView loginView;
//        LoginViewModel loginViewModel;
//        LoggedInViewModel loggedInViewModel;
//        TempLoggedInView loggedInView;
//        LoggedInViewModel loggedInViewModel;

        cardPanels.setLayout(cardLayout);

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

        // add login view to app
        LoginViewModel loginViewModel = new LoginViewModel();
        LoginView loginView = new LoginView(loginViewModel);
        cardPanels.add(loginView, loginView.getViewName());

        // placeholder logged in screen
        LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
        TempLoggedInView loggedInView = new TempLoggedInView(loggedInViewModel);
        cardPanels.add(loggedInView, loggedInView.getViewName());

        // data access layer
        LoginUseCaseOutputBoundary loginUseCaseOutputBoundary = new LoginPresenter(
                loginViewModel,
                loggedInViewModel,
                viewManagerModel
        );
        LoginUseCaseInputBoundary loginInteractor = new LoginUseCaseInteractor(
                demo,
                loginUseCaseOutputBoundary
        );
        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);

        JFrame application = new JFrame("Login");
        application.setSize(400, 200);
        application.setContentPane(loginView.getPane());
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.add(cardPanels);
        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChanged();
        // TODO: at least figure out why this works...
        // TODO: rework card system
        application.pack();
        //
        application.setVisible(true);
    }
}
