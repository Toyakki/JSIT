package view;

import javax.swing.*;
import java.awt.*;

public class LoginView {
    public static void main(String[] args) {
        // Creates Email Obj.
        JLabel email_label = new JLabel("Email");
        JTextArea email_textbox = new JTextArea();
        JLabel email_example_label = new JLabel("eg: name@example.com");
        email_example_label.setForeground(Color.lightGray);

        // Creates Password Obj.
        JLabel password_label = new JLabel("Password");
        JLabel password_example_label = new JLabel("<html>16 characters, 1 upper case, 1 lower case, 1 number<br>and 1 special character (!@#$%^&*)</html>");
        password_example_label.setForeground(Color.lightGray);
        JTextArea password_textbox = new JTextArea();

        // Creates Button Obj.
        JButton login_button = new JButton("Login");
        JButton sign_up_button = new JButton("Sign Up");

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
        JPanel main_panel = new JPanel();
        main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
        main_panel.add(email_panel);
        main_panel.add(password_panel);
        main_panel.add(login_button);
        main_panel.add(sign_up_button);

        // Creates Frame
        JFrame frame = new JFrame("Login");
        frame.setSize(400, 200);
        frame.setContentPane(main_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
