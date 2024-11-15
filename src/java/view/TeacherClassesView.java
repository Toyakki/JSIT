package view;

import interface_adapters.teacher.TeacherClassesState;
import interface_adapters.teacher.TeacherClassesViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class TeacherClassesView extends JPanel implements ActionListener, PropertyChangeListener {
    private TeacherClassesViewModel classesViewModel;
    private final String viewName = "teacher classes";
    private final JLabel noCoursesLabel = new JLabel("No courses created yet.");
    private final JLabel titleLabel = new JLabel("Classes:");

    public TeacherClassesView(TeacherClassesViewModel classesViewModel) {
        this.classesViewModel = classesViewModel;

        this.classesViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private void setFields(TeacherClassesState state){
        this.classesViewModel.setState(state);
        renderCourses();
    }

    private void renderCourses(){
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

    private JLabel createCourseLabel(String courseName){
        return new JLabel(courseName);
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
