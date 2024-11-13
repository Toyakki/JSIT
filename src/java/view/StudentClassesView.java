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
    private final String viewName = "TempLoggedInView";
    private final LoggedInViewModel viewModel;
    private LoggedInController loggedInController;
    private final JLabel label;

    public StudentClassesView(LoggedInViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        this.label = new JLabel("work in progress");
        this.add(label);

    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
    }

    public void propertyChange(PropertyChangeEvent evt) {System.out.println(evt.getPropertyName());}

    public void setFields(LoggedInState state) {
        this.viewModel.setState(state);
    }

    public String getViewName(){
        return viewName;
    }

}
