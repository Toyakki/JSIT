package view;

import interface_adapters.student.StudentClassesState;
import interface_adapters.student.StudentClassesViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class StudentClassesView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "student classes";
    private final StudentClassesViewModel classesViewModel;
    private final JLabel noCoursesLabel = new JLabel("No courses joined yet.");
    private final JLabel titleLabel = new JLabel("Classes:");

    public StudentClassesView(StudentClassesViewModel viewModel) {
        this.classesViewModel = viewModel;

        this.classesViewModel.addPropertyChangeListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void actionPerformed(ActionEvent e) { }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            setFields(this.classesViewModel.getState());
        }
    }

    private void setFields(StudentClassesState state) {
        this.classesViewModel.setState(state);
        renderCourses();
    }

    private void renderCourses() {
        if (this.classesViewModel.getState().getCourseNames().isEmpty() && this.getComponentCount() == 0) {
            add(noCoursesLabel);
        } else if (!this.classesViewModel.getState().getCourseNames().isEmpty()) {
            remove(this.noCoursesLabel);
            add(this.titleLabel);
            List<String> courseNames = this.classesViewModel.getState().getCourseNames();
            for (String courseName : courseNames) {
                add(createCourseLabel(courseName));
            }
        }
    }

    public String getViewName(){
        return viewName;
    }

    private JLabel createCourseLabel(String courseName) {
        return new JLabel(courseName);
    }

}
