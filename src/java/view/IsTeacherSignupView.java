package view;

import interface_adapters.sign_up.SignUpController;
import interface_adapters.ViewManagerModel;

import javax.swing.*;
import java.awt.*;

public class IsTeacherSignupView extends JPanel {
    private final SignUpController signUpController;
    private final String viewName = "IsTeacherSignUpView";
    private ViewManagerModel viewManagerModel;

    public IsTeacherSignupView(SignUpController signUpController,
                               ViewManagerModel viewManagerModel) {
        this.signUpController = signUpController;
        this.viewManagerModel = viewManagerModel;
        setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel("Would you like to create a teacher account?");
        add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public String getViewName() {
        return this.viewName;
    }
}