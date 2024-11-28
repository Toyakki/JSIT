package view;

import interface_adapters.create_assignment.AssignmentCreaterController;
import interface_adapters.download.DownloadController;
import interface_adapters.grade.GradeController;
import interface_adapters.teacher_course.TeacherCourseBackController;
import interface_adapters.teacher_course.TeacherCourseViewModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TeacherCourseView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "teacher course";
    private final TeacherCourseViewModel teacherCourseViewModel;
    private final String[] columnNames = {"email", "download", "feedback", "submitted", "grade"};
    private TeacherCourseBackController teacherCourseBackController;
    private AssignmentCreaterController assignmentCreaterController;
    private DownloadController downloadController;
    private GradeController gradeController;
//    private UploadController uploadController;
    private File newAssignmentFile;

    JLabel courseLabel;
    JPanel createAssignmentPanel;
    Map<String, JPanel> assignmentPanels = new HashMap<>();

    public TeacherCourseView(TeacherCourseViewModel viewModel, TeacherCourseBackController teacherCourseBackController,
                             AssignmentCreaterController assignmentCreaterController, DownloadController downloadController,
                             GradeController gradeController) {
        this.teacherCourseViewModel = viewModel;
        this.teacherCourseBackController = teacherCourseBackController;
        this.assignmentCreaterController = assignmentCreaterController;
        this.downloadController = downloadController;
        this.gradeController = gradeController;
//        this.uploadController = uploadController;

        this.teacherCourseViewModel.addPropertyChangeListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        courseLabel = new JLabel(teacherCourseViewModel.getState().getCourseName());
        add(courseLabel);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            teacherCourseBackController.back(teacherCourseViewModel.getState().getEmail());
        });
        this.add(backButton);

        createAssignmentPanel = buildCreateAssignmentPanel();
        createAssignmentPanel.setPreferredSize(new Dimension(600, 100));
        add(createAssignmentPanel);
    }

    public void actionPerformed(ActionEvent e) { }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            courseLabel.setText(teacherCourseViewModel.getState().getCourseName());
            renderAssignments();
        }
    }

    public String getViewName(){
        return this.viewName;
    }

    private void clearView(){
        if (!this.assignmentPanels.isEmpty()){
            for (String assignmentName : this.assignmentPanels.keySet()){
                remove(this.assignmentPanels.get(assignmentName));
            }
        }
        this.assignmentPanels.clear();
    }

    private void setFields(){
        remove(createAssignmentPanel);
        createAssignmentPanel = buildCreateAssignmentPanel();
        add(createAssignmentPanel);
    }

    public void renderAssignments(){
        clearView();
        setFields();
        System.out.println(teacherCourseViewModel.getState().getAssignmentsNames().size());
        for (int i = 0; i < teacherCourseViewModel.getState().getAssignmentsNames().size(); i++) {
            JPanel assignmentPanel = new JPanel();
            assignmentPanel.setPreferredSize(new Dimension(600, 100));

            assignmentPanel.add(new JLabel(teacherCourseViewModel.getState().getAssignmentsNames().get(i)));
            assignmentPanel.add(new JLabel(teacherCourseViewModel.getState().getAssignmentsDueDates().get(i)));
            assignmentPanel.add(new JLabel(teacherCourseViewModel.getState().getAssignmentsMarks().get(i)));

            JTable assignmentsTable = new JTable(teacherCourseViewModel.getState().getStudentEmails().size(), 4);
            assignmentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            assignmentPanel.add(assignmentsTable);
            this.assignmentPanels.put(
                    teacherCourseViewModel.getState().getAssignmentsNames().get(i),
                    assignmentPanel
            );
            add(assignmentPanel);

            if (teacherCourseViewModel.getState().getStudentEmails().isEmpty()){
                break;
            }

            for (int x = 0; x < 5; x++){
                assignmentsTable.setValueAt(columnNames[x], x, 0);
            }

            for (int x = 0; x < teacherCourseViewModel.getState().getStudentEmails().size(); x++){
                assignmentsTable.setValueAt(teacherCourseViewModel.getState().getStudentEmails().get(x), 0, x + 1);

                if (!teacherCourseViewModel.getState().getAssignmentsStages().get(i).equals("assigned")){
                    JButton downloadButton = new JButton("Download");
                    assignmentsTable.setValueAt(downloadButton, 1, x + 1);
                    final String email = teacherCourseViewModel.getState().getStudentEmails().get(x);
                    downloadButton.addActionListener(e -> {
                        downloadController.download(teacherCourseViewModel.getState().getCourseName(), email, "submitted");
                    });
                    add(downloadButton);
                }

                if (teacherCourseViewModel.getState().getAssignmentsStages().get(i).equals("graded")){
                    JButton gradingButton = new JButton("graded/download");
                    assignmentsTable.setValueAt(gradingButton, 2, x + 1);
                    final String email = teacherCourseViewModel.getState().getStudentEmails().get(x);
                    gradingButton.addActionListener(e -> {
                        downloadController.download(teacherCourseViewModel.getState().getCourseName(), email, "graded");
                    });
                    add(gradingButton);
                }
                else {
                    JButton gradingButton = new JButton("not graded/upload");
                    assignmentsTable.setValueAt(gradingButton, 2, x + 1);
                    gradingButton.addActionListener(e -> {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setAcceptAllFileFilterUsed(false);
                        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only PDFs", "pdf");
                        fileChooser.addChoosableFileFilter(restrict);
                        int fileSelectionStatus = fileChooser.showDialog(null, "Upload");
                        if (fileSelectionStatus == JFileChooser.APPROVE_OPTION) {
//                            uploadController.uploadGraded(fileChooser.getSelectedFile(), teacherCourseViewModel.getState().getStudentEmails().get(index), teacherCourseViewModel.getState().getAssignmentsNames());
                        }
                    });
                    add(gradingButton);
                }

                if (teacherCourseViewModel.getState().getAssignmentsStages().get(i).equals("assigned")){
                    assignmentsTable.setValueAt("not submitted", 3, x + 1);
                }
                else {
                    assignmentsTable.setValueAt("submitted", 3, x + 1);
                }

                JTextField gradeField = new JTextField();
                assignmentsTable.setValueAt(gradeField, 4, x + 1);
                gradeField.addActionListener(e -> {
                    gradeController.setGrade(gradeField.getText());
                    // this right
                });

            }
        }
    }

    private JPanel buildCreateAssignmentPanel(){
        JPanel createAssignmentPanel = new JPanel();
        createAssignmentPanel.add(new JLabel("Create Assignment"));
        JButton uploadButton = new JButton("Upload");
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only PDFs", "pdf");
            fileChooser.addChoosableFileFilter(restrict);
            int fileSelectionStatus = fileChooser.showDialog(null, "Upload");
            if (fileSelectionStatus == JFileChooser.APPROVE_OPTION) {
                newAssignmentFile = fileChooser.getSelectedFile();
            }
        });
        createAssignmentPanel.add(uploadButton);

        createAssignmentPanel.add(new JLabel("Set Due Date, e.g. Jan. 1"));
        JTextField dueDate = new JTextField();
        createAssignmentPanel.add(dueDate);

        JTextField totalGradeField = new JTextField("Total Grade");
        createAssignmentPanel.add(totalGradeField);

        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> {
            if (newAssignmentFile.length() == 0) {
                JOptionPane.showMessageDialog(null, "Assignment not Selected");
            }
            else {
                String assignmentName = ""; // need to replace with an actual naming scheme
                assignmentCreaterController.createAssignment(
                        this.teacherCourseViewModel.getState().getEmail(),
                        assignmentName,
                        this.teacherCourseViewModel.getState().getCourseName(),
                        dueDate.getText(),
                        totalGradeField.getText(),
                        newAssignmentFile
                );
            }
        });
        createAssignmentPanel.add(createButton);
        return createAssignmentPanel;
    }
}
