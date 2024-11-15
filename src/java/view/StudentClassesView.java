package view;

import interface_adapters.student.StudentClassesState;
import interface_adapters.student.StudentClassesViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

        noCoursesLabel.setFont(new Font("Tomaha", Font.BOLD, 20));
        titleLabel.setFont(new Font("Tomaha", Font.BOLD, 20));

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
            addBlankSpace();
            add(titleLabel);
            addBlankSpace(2);
            List<String> courseNames = this.classesViewModel.getState().getCourseNames();
            for (String courseName : courseNames) {
                add(createCourseLabel(courseName));
                addBlankSpace();
            }
        }
    }

    public String getViewName(){
        return viewName;
    }

    private JLabel createCourseLabel(String courseName) {
        JLabel courseLabel = new JLabel(courseName);
        courseLabel.setFont(new Font("Tomaha", Font.PLAIN, 16));
        courseLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                System.out.println(courseName);
            }

            @Override
            public void mouseEntered(MouseEvent e){
                courseLabel.setForeground(Color.lightGray);
            }

            @Override
            public void mouseExited(MouseEvent e){
                courseLabel.setForeground(Color.black);
            }
        });
        return courseLabel;
    }

    private void addBlankSpace(int n) {
        for (int i = 0; i < n; i++) {
            add(new JLabel(" "));
        }
    }

    private void addBlankSpace(){
        addBlankSpace(1);
    }
}
