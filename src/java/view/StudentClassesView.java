package view;

import interface_adapters.student.StudentClassesState;
import interface_adapters.student.StudentClassesViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StudentClassesView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "student classes";
    private final StudentClassesViewModel classesViewModel;
    private final JLabel wip_label = new JLabel("work in progress");
    private final JLabel email_label = new JLabel("email: ");
    private final JLabel type_label = new JLabel("type: student");

    public StudentClassesView(StudentClassesViewModel viewModel) {
        this.classesViewModel = viewModel;

        setFields(this.classesViewModel.getState());

        this.classesViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(wip_label);
        add(email_label);
        add(type_label);
    }

    public void actionPerformed(ActionEvent e) { }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            setFields(this.classesViewModel.getState());
        }
    }

    public void setFields(StudentClassesState state) {
        this.classesViewModel.setState(state);
        email_label.setText("email: " + state.getEmail());
    }

    public String getViewName(){
        return viewName;
    }

}
