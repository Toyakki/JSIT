package view;

import interface_adapters.join_class.JoinCourseController;
import interface_adapters.student.StudentClassesState;
import interface_adapters.student.StudentClassesViewModel;
import interface_adapters.student.StudentCourseViewController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentClassesView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "student classes";
    private final StudentClassesViewModel classesViewModel;
    private final StudentCourseViewController courseViewController;
    private final JoinCourseController joinCourseController;
    private final JLabel noCoursesLabel = new JLabel("No courses joined yet.");
    private final JLabel titleLabel = new JLabel(" Classes:");

    private final JLabel errorLabel = new JLabel();

    private final Map<String, JLabel> courseLabels = new HashMap<>();
    private final JPanel coursesPanel = new JPanel();
    private final JPanel joinCoursePanel = new JPanel();

    private final JButton joinButton = new JButton("Join Class");
    private final JTextField joinCodeField = new JTextField();

    public StudentClassesView(StudentClassesViewModel viewModel,
                              StudentCourseViewController controller,
                              JoinCourseController joinCourseController
    ) {
        this.courseViewController = controller;
        this.classesViewModel = viewModel;
        this.joinCourseController = joinCourseController;
        this.classesViewModel.addPropertyChangeListener(this);

        errorLabel.setFont(new Font("Tomaha", Font.BOLD, 16));
        errorLabel.setForeground(Color.RED);
        joinCoursePanel.add(errorLabel);

        noCoursesLabel.setFont(new Font("Tomaha", Font.BOLD, 20));
        titleLabel.setFont(new Font("Tomaha", Font.BOLD, 20));

        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        coursesPanel.setPreferredSize(new Dimension(370, 300));

        joinButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        joinCourseController.joinCourse(
                                viewModel.getState().getEmail(),
                                joinCodeField.getText()
                        );
                    }
                }
        );
        joinButton.setPreferredSize(new Dimension(100, 30));
        joinButton.setBackground(Color.BLACK);
        joinButton.setForeground(Color.WHITE);
        joinButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        joinButton.setBorderPainted(false);
        joinCodeField.setPreferredSize(new Dimension(100, 30));
        joinCoursePanel.setLayout(new BoxLayout(joinCoursePanel, BoxLayout.Y_AXIS));

        JPanel joinCodeFieldWrapper = new JPanel();
        joinCodeFieldWrapper.add(joinCodeField);
        joinCodeFieldWrapper.setPreferredSize(new Dimension(100, 30));

        joinCoursePanel.setPreferredSize(new Dimension(100, 70));
        joinCoursePanel.add(joinCodeFieldWrapper);
        joinCodeFieldWrapper.add(joinButton);

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(coursesPanel);
        add(joinCoursePanel);
    }

    public void actionPerformed(ActionEvent e) { }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            setFields(this.classesViewModel.getState());
        }
    }

    private void setFields(StudentClassesState state) {
        this.classesViewModel.setState(state);
        this.errorLabel.setText(this.classesViewModel.getState().getError());
        renderCourses();
    }

    private void clearView(){
        if (coursesPanel.getComponentCount() == 0){
            return;
        }
        for (String courseName : courseLabels.keySet()) {
            coursesPanel.remove(this.courseLabels.get(courseName));
        }
        courseLabels.clear();
    }

    private void renderCourses() {
        clearView();
        if (this.classesViewModel.getState().getCourseNames().isEmpty() && coursesPanel.getComponentCount() == 0) {
            courseLabels.put("no courses", noCoursesLabel);
            coursesPanel.add(noCoursesLabel);
        } else if (!this.classesViewModel.getState().getCourseNames().isEmpty()) {
            coursesPanel.remove(this.noCoursesLabel);
            courseLabels.remove("no courses");

            coursesPanel.add(titleLabel);
            List<String> courseNames = this.classesViewModel.getState().getCourseNames();
            for (String courseName : courseNames) {
                JLabel courseLabel = createCourseLabel(courseName);
                courseLabels.put(courseName, courseLabel);
                coursesPanel.add(courseLabel);
            }
        }
        coursesPanel.revalidate();
        coursesPanel.repaint();
    }

    public String getViewName(){
        return viewName;
    }

    private JLabel createCourseLabel(String courseName) {
        JLabel courseLabel = new JLabel("   " + courseName);
        courseLabel.setFont(new Font("Tomaha", Font.PLAIN, 16));
        final StudentCourseViewController controller = this.courseViewController;
        final StudentClassesViewModel viewModel = this.classesViewModel;
        courseLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                controller.viewCourse(
                        viewModel.getState().getEmail(),
                        courseLabel.getText().strip()
                );
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

    private void addBlankSpace(JPanel p) {
        p.add(new JLabel(" "));
    }

    private void addBlankSpace(JPanel p, int n){
        for (int i = 0; i < n; i++) {
            p.add(new JLabel(" "));
        }
    }
}
