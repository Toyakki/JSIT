package view;

import interface_adapters.teacher.TeacherClassesState;
import interface_adapters.teacher.TeacherClassesViewModel;
import users.UserOutputData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TeacherClassesView extends JPanel implements ActionListener, PropertyChangeListener {
    private TeacherClassesViewModel classesViewModel;
    private final String viewName = "teacher classes";

    private final JLabel wip_label = new JLabel("work in progress");
    private JLabel email_label = new JLabel("email: ");
    private final JLabel type_label = new JLabel("type: teacher");

    public TeacherClassesView(TeacherClassesViewModel classesViewModel) {
        this.classesViewModel = classesViewModel;
        this.classesViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(wip_label);
        add(email_label);
        add(type_label);
    }

    public void setFields(TeacherClassesState state){
        this.classesViewModel.setState(state);
        email_label.setText("email: " + state.getEmail());
    }

    public void actionPerformed(ActionEvent e) { }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            setFields(this.classesViewModel.getState());
        }
    }

    public String getViewName(){
        return this.viewName;
    }
}
