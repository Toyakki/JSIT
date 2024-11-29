package view;

import interface_adapters.create_assignment.AssignmentCreaterController;
import interface_adapters.download.DownloadController;
import interface_adapters.grade.GradeController;
import interface_adapters.teacher_course.TeacherCourseBackController;
import interface_adapters.teacher_course.TeacherCourseState;
import interface_adapters.teacher_course.TeacherCourseViewModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
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
            this.teacherCourseBackController.back(teacherCourseViewModel.getState().getEmail());
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
        if (teacherCourseViewModel.getState().getStudentEmails().isEmpty()){
            return;
        }
        for (int i = 0; i < teacherCourseViewModel.getState().getAssignmentsNames().size(); i++) {

            JPanel assignmentPanel = new JPanel();

            assignmentPanel.add(new JLabel(teacherCourseViewModel.getState().getAssignmentsNames().get(i)));
            assignmentPanel.add(new JLabel(teacherCourseViewModel.getState().getAssignmentsDueDates().get(i)));
            assignmentPanel.add(new JLabel(teacherCourseViewModel.getState().getAssignmentBaseMarks().get(i)));

            String[][] assignmentData = new String[teacherCourseViewModel.getState().getStudentEmails().size()][columnNames.length];
            JButton[][] actionListeners = new JButton[teacherCourseViewModel.getState().getStudentEmails().size()][columnNames.length];

            for (int j = 0; j < teacherCourseViewModel.getState().getStudentEmails().size(); j++) {
                TeacherCourseState state = teacherCourseViewModel.getState();
                String studentEmail = teacherCourseViewModel.getState().getStudentEmails().get(j);
                String assignmentStage = state.getAssignmentsStages().get(i).get(studentEmail);
                int index = j;
                assignmentData[j][0] = teacherCourseViewModel.getState().getStudentEmails().get(j);
                if (!assignmentStage.equals("assigned")){
                    JButton downloadButton = new JButton("Download");
                    downloadButton.addActionListener(e -> {
                        downloadController.download(state.getCourseName(), state.getStudentEmails().get(index), "submitted");
                    });
                    actionListeners[j][1] = downloadButton;
                    assignmentData[j][1] = "Download";
                } else {
                    assignmentData[j][1] = "   ";
                }

                assignmentData[j][3] = assignmentStage;

                if (assignmentStage.equals("graded")){
                    JButton downloadFeedbackButton = new JButton("Feedback");
                    downloadFeedbackButton.addActionListener(e -> {
                        downloadController.download(state.getCourseName(), state.getStudentEmails().get(index), "graded");
                    });
                    actionListeners[j][2] = downloadFeedbackButton;
                    assignmentData[j][2] = "Feedback";
                } else {
                    assignmentData[j][2] = "   ";
                }

                JButton gradeButton = new JButton("Grade");
                gradeButton.addActionListener(e -> {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only PDFs", "pdf");
                    fileChooser.addChoosableFileFilter(restrict);
                    int fileSelectionStatus = fileChooser.showDialog(null, "Upload");
                    if (fileSelectionStatus == JFileChooser.APPROVE_OPTION) {
                        System.out.println("Selected: " + fileChooser.getSelectedFile().getName());
//                      uploadController.uploadGraded(fileChooser.getSelectedFile(), teacherCourseViewModel.getState().getStudentEmails().get(index), teacherCourseViewModel.getState().getAssignmentsNames());
                    }
                });
                actionListeners[j][4] = gradeButton;
                assignmentData[j][4] = "Grade";
            }

            JTable assignmentsTable = new JTable(
                    assignmentData, columnNames
            );

            MouseListener tableListener = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = assignmentsTable.getSelectedRow();
                    int col = assignmentsTable.getSelectedColumn();

                    if (row != -1 && col != -1) {
                        if (actionListeners[row][col] != null){
                            actionListeners[row][col].doClick();
                            System.out.println(assignmentData[row][col]);
                        }
                    }
                }
            };

            assignmentsTable.addMouseListener(tableListener);

            assignmentPanel.add(assignmentsTable);
            this.assignmentPanels.put(
                    teacherCourseViewModel.getState().getAssignmentsNames().get(i),
                    assignmentPanel
            );
            add(assignmentPanel);
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
