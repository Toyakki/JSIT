package view;

import interface_adapters.student.StudentClassesState;
import interface_adapters.student.StudentClassesViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class StudentClassesView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "student classes";
    private final StudentClassesViewModel classesViewModel;
    private final JLabel noCoursesLabel = new JLabel("No courses joined yet.");

    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu coursesMenu = new JMenu("Courses");

    public StudentClassesView(StudentClassesViewModel viewModel) {
        this.classesViewModel = viewModel;
        this.classesViewModel.addPropertyChangeListener(this);

        menuBar.add(coursesMenu);
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
            add(menuBar);
            List<String> courseNames = this.classesViewModel.getState().getCourseNames();
            for (String courseName : courseNames) {
                coursesMenu.add(renderCourse(courseName));
            }
        }
    }

    public String getViewName(){
        return viewName;
    }

    private JMenuItem renderCourse(String courseName) {
        JMenuItem course = new JMenuItem(courseName);
        course.addActionListener(
                (e) -> {
                    System.out.println(courseName);
                }
        );
        return course;
    }

}
