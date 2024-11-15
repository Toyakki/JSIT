package view;

import interface_adapters.teacher.TeacherClassesState;
import interface_adapters.teacher.TeacherClassesViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

        noCoursesLabel.setFont(new Font("Tomaha", Font.BOLD, 20));
        titleLabel.setFont(new Font("Tomaha", Font.BOLD, 20));

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
            addBlankSpace();
            add(this.titleLabel);
            addBlankSpace(2);
            List<String> courseNames = this.classesViewModel.getState().getCourseNames();
            for (String courseName : courseNames) {
                add(createCourseLabel(courseName));
                addBlankSpace();
            }
        }
    }

    private JLabel createCourseLabel(String courseName){
        JLabel courseLabel = new JLabel(courseName);
        courseLabel.setFont(new Font("Tomaha", Font.PLAIN, 16));
        courseLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(courseName);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                courseLabel.setForeground(Color.lightGray);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                courseLabel.setForeground(Color.black);
            }
        });
        return courseLabel;
    }

    private void addBlankSpace(int n){
        for (int i = 0; i < n; i++) {
            add(new JLabel(" "));
        }
    }

    private void addBlankSpace(){
        addBlankSpace(1);
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
