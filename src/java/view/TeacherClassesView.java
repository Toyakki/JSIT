package view;

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
    private TeacherCourseBackController teacherCourseBackController;
    private TeacherCourseViewController courseViewController;
    private final String viewName = "teacher classes";
    private final JLabel noCoursesLabel = new JLabel("No courses created yet.");
    private final JLabel titleLabel = new JLabel("Classes:");
    private final JPanel coursesPanel = new JPanel();

    private final Map<String, JLabel> courseLabels = new HashMap<>();

    private final JPanel joinCoursePanel = new JPanel();
    private final JButton joinButton = new JButton("Create Class");
    private final JTextField joinCodeField = new JTextField();

    public TeacherClassesView(TeacherClassesViewModel classesViewModel,
                              TeacherCourseBackController teacherCourseBackController,
                              TeacherCourseViewController courseViewController) {
        this.classesViewModel = classesViewModel;
        this.teacherCourseBackController = teacherCourseBackController;
        this.courseViewController = courseViewController;
        this.classesViewModel.addPropertyChangeListener(this);

        noCoursesLabel.setFont(new Font("Tomaha", Font.BOLD, 20));
        titleLabel.setFont(new Font("Tomaha", Font.BOLD, 20));

        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        coursesPanel.setPreferredSize(new Dimension(370, 300));

        joinButton.setPreferredSize(new Dimension(125, 30));
        joinButton.setBackground(Color.BLACK);
        joinButton.setForeground(Color.WHITE);
        joinButton.setFont(new Font("Tomaha", Font.BOLD, 12));
        joinButton.setBorderPainted(false);
        joinCodeField.setPreferredSize(new Dimension(125, 30));
        joinCoursePanel.setLayout(new BoxLayout(joinCoursePanel, BoxLayout.Y_AXIS));

        JPanel joinCodeFieldWrapper = new JPanel();
        joinCodeFieldWrapper.add(joinCodeField);
        joinCodeFieldWrapper.setPreferredSize(new Dimension(125, 30));

        joinCoursePanel.setPreferredSize(new Dimension(125, 70));
        joinCoursePanel.add(joinCodeFieldWrapper);
        joinCodeFieldWrapper.add(joinButton);

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(coursesPanel);
        add(joinCoursePanel);
    }

    private void setFields(TeacherClassesState state){
        this.classesViewModel.setState(state);
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
//            addBlankSpace(coursesPanel);
            coursesPanel.add(this.titleLabel);
//            addBlankSpace(coursesPanel, 2);
            List<String> courseNames = this.classesViewModel.getState().getCourseNames();
            for (String courseName : courseNames) {
                JLabel courseLabel = createCourseLabel(courseName);
                courseLabels.put(courseName, courseLabel);
                coursesPanel.add(courseLabel);
//                addBlankSpace(coursesPanel);
            }
        }
    }

    private void addBlankSpace(JPanel p, int n) {
        for (int i = 0; i < n; i++){
            p.add(new JLabel(" "));
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

    private void addBlankSpace(int n){
        for (int i = 0; i < n; i++) {
            add(new JLabel(" "));
        }
    }

    private void addBlankSpace(){
        addBlankSpace(1);
    }

    private void addBlankSpace(JPanel p){
        p.add(new JLabel(" "));
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
