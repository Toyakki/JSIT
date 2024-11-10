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

public class TempLoggedInView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "TempLoggedInView";
    private final LoggedInViewModel viewModel;
    private LoggedInController loggedInController;
    private final JLabel label;

    public TempLoggedInView(LoggedInViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        this.label = new JLabel("work in progress");
        this.add(label);

    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
    }

    public void propertyChange(PropertyChangeEvent evt) {
//        final LoggedInState state = (LoggedInState) evt.getSource();
//        setFields(state);
    }

    public void setFields(LoggedInState state) {

    }

    public String getViewName(){
        return viewName;
    }

    public void setLoginController(LoggedInController loggedInController) {
        this.loggedInController = loggedInController;
    }
}
