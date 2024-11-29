package view;

import interface_adapters.create_class.CreateCourseController;
import interface_adapters.teacher_course.TeacherCourseBackController;
import interface_adapters.teacher.TeacherClassesState;
import interface_adapters.teacher.TeacherClassesViewModel;
import interface_adapters.teacher.TeacherCourseViewController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherClassesView extends JPanel implements ActionListener, PropertyChangeListener {
    private TeacherClassesViewModel classesViewModel;
    private TeacherCourseViewController courseViewController;
    private CreateCourseController createCourseController;
    private final String viewName = "teacher classes";
    private final JLabel noCoursesLabel = new JLabel("No courses created yet.");
    private final JLabel titleLabel = new JLabel("Classes:");
    private final JPanel coursesPanel = new JPanel();

    private final Map<String, JLabel> courseLabels = new HashMap<>();

    private final JPanel createCoursePanel = new JPanel();
    private final JButton createButton = new JButton("Create Class");
    private final JTextField courseNameField = new JTextField();

    public TeacherClassesView(TeacherClassesViewModel classesViewModel,
                              TeacherCourseViewController courseViewController,
                              CreateCourseController createCourseController) {
        this.classesViewModel = classesViewModel;
        this.createCourseController = createCourseController;
        this.courseViewController = courseViewController;
        this.classesViewModel.addPropertyChangeListener(this);

        noCoursesLabel.setFont(new Font("Tomaha", Font.BOLD, 20));
        titleLabel.setFont(new Font("Tomaha", Font.BOLD, 20));

        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        coursesPanel.setPreferredSize(new Dimension(370, 300));

        courseNameField.setPreferredSize(new Dimension(125, 30));
        createCoursePanel.setLayout(new BoxLayout(createCoursePanel, BoxLayout.Y_AXIS));

        createButton.setPreferredSize(new Dimension(125, 30));
        createButton.setBackground(Color.BLACK);
        createButton.setForeground(Color.WHITE);
        createButton.setFont(new Font("Tomaha", Font.BOLD, 12));
        createButton.setBorderPainted(false);

        JPanel joinCodeFieldWrapper = new JPanel();
        joinCodeFieldWrapper.add(courseNameField);
        joinCodeFieldWrapper.setPreferredSize(new Dimension(125, 30));

        createCoursePanel.setPreferredSize(new Dimension(125, 70));
        createCoursePanel.add(joinCodeFieldWrapper);
        joinCodeFieldWrapper.add(createButton);

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(coursesPanel);
        add(createCoursePanel);
    }

    private void setFields(TeacherClassesState state){
        this.classesViewModel.setState(state);
        final String email = state.getEmail();
        courseNameField.setText("");
        for (ActionListener actionListener : createButton.getActionListeners()) {
            createButton.removeActionListener(actionListener);
        }
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCourseController.createCourse(email, courseNameField.getText());
            }
        });
        renderCourses();
    }

    public void clearView(){
        if (coursesPanel.getComponentCount() == 0){
            return;
        }
        for (String courseName : courseLabels.keySet()){
            coursesPanel.remove(this.courseLabels.get(courseName));
        }
        courseLabels.clear();
    }

    private void renderCourses(){
        clearView();
        if (this.classesViewModel.getState().getCourseNames().isEmpty() && coursesPanel.getComponentCount() == 0) {
            courseLabels.put("no courses", noCoursesLabel);
            coursesPanel.add(noCoursesLabel);
        } else if (!this.classesViewModel.getState().getCourseNames().isEmpty()) {
            coursesPanel.remove(this.noCoursesLabel);
            courseLabels.remove("no courses");
            coursesPanel.add(this.titleLabel);
            List<String> courseNames = this.classesViewModel.getState().getCourseNames();
            for (String courseName : courseNames) {
                JLabel courseLabel = createCourseLabel(courseName);
                courseLabels.put(courseName, courseLabel);
                coursesPanel.add(courseLabel);
            }
        }
    }

    private JLabel createCourseLabel(String courseName){
        JLabel courseLabel = new JLabel("   " + courseName);
        courseLabel.setFont(new Font("Tomaha", Font.PLAIN, 16));
        final TeacherCourseViewController controller = this.courseViewController;
        final TeacherClassesViewModel viewModel = this.classesViewModel;
        courseLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.viewCourse(
                        viewModel.getState().getEmail(),
                        courseLabel.getText().strip()
                );
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
