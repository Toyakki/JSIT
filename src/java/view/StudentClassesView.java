package view;

import interface_adapters.logged_in.LoggedInController;
import interface_adapters.logged_in.LoggedInState;
import interface_adapters.logged_in.LoggedInViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StudentClassesView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "student classes view";
    private final LoggedInViewModel viewModel;
    private LoggedInController loggedInController;
    private final JLabel wip_label = new JLabel("work in progress");
    private final JLabel email_label = new JLabel("email: ");
    private final JLabel type_label = new JLabel("type: ");

    public StudentClassesView(LoggedInViewModel viewModel) {
        this.viewModel = viewModel;

        setFields(this.viewModel.getState());

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(wip_label);
        add(email_label);
        add(type_label);
    }

    public void actionPerformed(ActionEvent e) { }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            setFields(this.viewModel.getState());
        }
    }

    public void setFields(LoggedInState state) {
        this.viewModel.setState(state);
        email_label.setText("email: " + state.getEmail());
        type_label.setText("type: " + state.getType());
    }

    public String getViewName(){
        return viewName;
    }

}
