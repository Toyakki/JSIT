package view;

import interface_adapters.logged_in.LoggedInViewModel;
import interface_adapters.login.LoginController;
import interface_adapters.login.LoginState;
import interface_adapters.login.LoginViewModel;

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

    private JTextArea email_textbox;
    private JTextArea password_textbox;

    private JPanel main_panel;

    public LoginView(LoginViewModel loginViewModel) {
        // Assign backend components
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addPropertyChangeListener(this);

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
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource().equals(login_button)){
                            final LoginState currentState = loginViewModel.getState();
                            loginController.login(
                                    currentState.getEmail(),
                                    currentState.getPassword(),
                                    currentState.getType()
                            );
                        }
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
        main_panel = new JPanel();
        main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
        main_panel.add(email_panel);
        main_panel.add(password_panel);
        main_panel.add(login_button);
        main_panel.add(sign_up_button);

        // Creates Frame (moved to Main.java)
//        JFrame frame = new JFrame("Login");
//        frame.setSize(400, 200);
//        frame.setContentPane(main_panel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        System.out.println(e.getActionCommand());
    }

    public void propertyChange(PropertyChangeEvent evt) {
//        final LoginState state = (LoginState) evt.getSource();
//        setFields(state);
        // eventually put error code here
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

    public Container getPane() {
        return this.main_panel;
    }
}
