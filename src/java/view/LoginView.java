package view;

import interface_adapters.login.LoginController;
import interface_adapters.login.LoginState;
import interface_adapters.login.LoginViewModel;
import interface_adapters.sign_up.SignUpController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "log in";
    private final LoginViewModel loginViewModel;

    private LoginController loginController;
    private SignUpController signUpController;

    private JTextArea email_textbox;
    private JTextArea password_textbox;

    private JPanel main_panel;

    public LoginView(LoginViewModel loginViewModel, LoginController loginController, SignUpController signUpController) {
        // Assign backend components
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addPropertyChangeListener(this);

        this.loginController = loginController;
        this.signUpController = signUpController;

        // creates error label
        JLabel error_label = new JLabel(" ");
        error_label.setForeground(Color.lightGray);

        // Creates Email Obj.
        JLabel email_label = new JLabel("Email");
        email_textbox = new JTextArea();
        JLabel email_example_label = new JLabel("eg: name@example.com");
        email_example_label.setForeground(Color.lightGray);

        // Creates Password Obj.
        JLabel password_label = new JLabel("Password");
        JLabel password_example_label = new JLabel("<html>16 characters, 1 upper case, 1 lower case, 1 number<br>and 1 special character (!@#$%^&*)</html>");
        password_example_label.setForeground(Color.lightGray);
        password_textbox = new JTextArea();

        // Creates Button Obj.
        JButton login_button = new JButton("Login");
        JButton sign_up_button = new JButton("Sign Up");

        // Log-in button functionality
        login_button.addActionListener(
                (e) -> {
                    if (e.getSource().equals(login_button)){
                        final LoginState currentState = loginViewModel.getState();
                        this.loginController.login(
                                currentState.getEmail(),
                                currentState.getPassword(),
                                currentState.getType()
                        );
                    }
                }
        );

        sign_up_button.addActionListener(
                (e) -> {
                    if (e.getSource().equals(sign_up_button)){
                        final LoginState currentState = loginViewModel.getState();
                        this.signUpController.createUser(
                                currentState.getEmail(),
                                currentState.getPassword(),
                                "student"
                        );
                    }
                }
        );

        // email field functionality
        email_textbox.getDocument().addDocumentListener(new DocumentListener() {
           private void documentListenerHelper(){
               final LoginState currentState = loginViewModel.getState();
               currentState.setEmail(email_textbox.getText());
               loginViewModel.setState(currentState);
           }

           public void insertUpdate(DocumentEvent e) {
               documentListenerHelper();
           }

           public void removeUpdate(DocumentEvent e) {
               documentListenerHelper();
           }

           public void changedUpdate(DocumentEvent e) {
               documentListenerHelper();
           }
        });

        // password field functionality
        password_textbox.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper(){
                final LoginState currentState = loginViewModel.getState();
                currentState.setPassword(password_textbox.getText());
                loginViewModel.setState(currentState);
            }

            public void insertUpdate(DocumentEvent e){
                documentListenerHelper();
            }

            public void removeUpdate(DocumentEvent e){
                documentListenerHelper();
            }

            public void changedUpdate(DocumentEvent e){
                documentListenerHelper();
            }
        });

        loginViewModel.addPropertyChangeListener(
                (evt) -> {
                    if (evt.getPropertyName().equals("error")) {
                        error_label.setText(loginViewModel.getState().getLoginError());
                    }
                }
        );

        // Creates Get Email Panel
        JPanel email_panel = new JPanel();
        email_panel.setLayout(new BoxLayout(email_panel, BoxLayout.Y_AXIS));
        email_panel.add(email_label);
        email_panel.add(email_textbox);
        email_panel.add(email_example_label);

        // Creates Get Password Panel
        JPanel password_panel = new JPanel();
        password_panel.setLayout(new BoxLayout(password_panel, BoxLayout.Y_AXIS));
        password_panel.add(password_label);
        password_panel.add(password_textbox);
        password_panel.add(password_example_label);

        // Creates Main Panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(error_label);
        add(email_panel);
        add(password_panel);
        add(login_button);
        add(sign_up_button);
    }

    public void actionPerformed(ActionEvent e){
        System.out.println(e.getActionCommand());
    }

    public void propertyChange(PropertyChangeEvent evt) {

    }

    public void setFields(LoginState state){
        email_textbox.setText(state.getEmail());
        password_textbox.setText(state.getPassword());
    }

    public String getViewName(){
        return viewName;
    }

    public void setLoginController(LoginController loginController){
        this.loginController = loginController;
    }

    public JPanel getPane() {
        return this;
    }
}
