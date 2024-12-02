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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
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
    private final String[] columnNames = {"email", "download", "feedback", "submitted","marks", "grade"};
    private TeacherCourseBackController teacherCourseBackController;
    private AssignmentCreaterController assignmentCreaterController;
    private DownloadController downloadController;
    private GradeController gradeController;
    private File newAssignmentFile;

    JLabel errorLabel = new JLabel();
    JPanel headerPanel;
    JLabel courseLabel;
    JLabel instructorLabel;
    JLabel codeLabel;
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

        this.teacherCourseViewModel.addPropertyChangeListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        errorLabel.setFont(new Font("Tomaha", Font.BOLD, 16));
        errorLabel.setForeground(Color.RED);
        add(errorLabel);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        add(headerPanel);

        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.setBorderPainted(false);
        backButton.setFont(new Font("Tomaha", Font.BOLD, 17));
        backButton.addActionListener(e -> {
            this.teacherCourseBackController.back(teacherCourseViewModel.getState().getEmail());
        });
        headerPanel.add(backButton);

        courseLabel = new JLabel(teacherCourseViewModel.getState().getCourseName());
        courseLabel.setFont(new Font("Tomaha", Font.BOLD, 15));
        headerPanel.add(courseLabel);

        instructorLabel = new JLabel("Instructor: " + teacherCourseViewModel.getState().getEmail());
        instructorLabel.setFont(new Font("Tomaha", Font.BOLD, 15));
        headerPanel.add(instructorLabel);

        codeLabel = new JLabel("Code: " + teacherCourseViewModel.getState().getCourseCode());
        codeLabel.setFont(new Font("Tomaha", Font.BOLD, 15));
        headerPanel.add(codeLabel);

        createAssignmentPanel = buildCreateAssignmentPanel();
        createAssignmentPanel.setPreferredSize(new Dimension(600, 100));
        add(createAssignmentPanel);
    }

    public void actionPerformed(ActionEvent e) { }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            errorLabel.setText(teacherCourseViewModel.getState().getError());
            courseLabel.setText("     " + teacherCourseViewModel.getState().getCourseName() + "      ");
            instructorLabel.setText("  Instructor: " + teacherCourseViewModel.getState().getEmail() + "     ");
            codeLabel.setText("  Code: " + teacherCourseViewModel.getState().getCourseCode());
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

    private void updateTableModel(String[][] newData, TableModel tableModel){
        for (int row = 0; row < newData.length; row++){
            for (int col = 0; col < newData[row].length; col++){
                tableModel.setValueAt(newData[row][col], row, col);
            }
        }
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

            TableModel tableModel = new DefaultTableModel(assignmentData, columnNames){
                @Override
                public boolean isCellEditable(int row, int col) {
                    return col == 4;
                }
            };

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
                    assignmentData[j][4] = state.getAssignmentsMarks().get(i).get(studentEmail);
                } else {
                    assignmentData[j][2] = "   ";
                    assignmentData[j][4] = "   ";
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
                        gradeController.gradeAssignment(tableModel.getValueAt(index, 4).toString(),
                                fileChooser.getSelectedFile(), state.getStudentEmails().get(index), state.getEmail(),
                                state.getCourseName(), state.getAssignmentsNames().get(index));
                    }
                });
                actionListeners[j][5] = gradeButton;
                assignmentData[j][5] = "Grade";
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
                        }
                    }
                }
            };

            updateTableModel(assignmentData, tableModel);
            assignmentsTable.setModel(tableModel);
            assignmentsTable.addMouseListener(tableListener);

            assignmentPanel.add(assignmentsTable);
            this.assignmentPanels.put(
                    teacherCourseViewModel.getState().getAssignmentsNames().get(i),
                    assignmentPanel
            );
            add(assignmentPanel);
        }
        this.revalidate();
        this.repaint();
    }

    private JPanel buildCreateAssignmentPanel(){
        JPanel createAssignmentPanel = new JPanel();
        createAssignmentPanel.add(new JLabel("Create Assignment"));
        JButton uploadButton = new JButton("Upload");
        uploadButton.setBackground(Color.BLACK);
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setFont(new Font("Tomaha", Font.BOLD, 14));
        uploadButton.setBorderPainted(false);
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

        JTextField nameField = new JTextField("Name");
        nameField.setFont(new Font("Tomaha", Font.BOLD, 14));
        nameField.setBackground(Color.BLACK);
        nameField.setForeground(Color.WHITE);
        createAssignmentPanel.add(nameField);

        JTextField dueDate = new JTextField("Due Date");
        dueDate.setFont(new Font("Tomaha", Font.BOLD, 14));
        dueDate.setBackground(Color.BLACK);
        dueDate.setForeground(Color.WHITE);
        createAssignmentPanel.add(dueDate);

        JTextField totalGradeField = new JTextField("Total Grade");
        totalGradeField.setFont(new Font("Tomaha", Font.BOLD, 14));
        totalGradeField.setBackground(Color.BLACK);
        totalGradeField.setForeground(Color.WHITE);
        createAssignmentPanel.add(totalGradeField);

        JButton createButton = new JButton("Create");
        createButton.setBackground(Color.BLACK);
        createButton.setForeground(Color.WHITE);
        createButton.setFont(new Font("Tomaha", Font.BOLD, 14));
        createButton.addActionListener(e -> {
            if (this.newAssignmentFile == null){
                this.teacherCourseViewModel.getState().setError("No file selected");
                this.teacherCourseViewModel.firePropertyChanged();
            }
            else if (this.newAssignmentFile.length() == 0) {
                JOptionPane.showMessageDialog(null, "Assignment not Selected");
            }
            else {
                String assignmentName = ""; // need to replace with an actual naming scheme
                assignmentCreaterController.createAssignment(
                        this.teacherCourseViewModel.getState().getEmail(),
                        nameField.getText(),
                        this.teacherCourseViewModel.getState().getCourseName(),
                        dueDate.getText(),
                        totalGradeField.getText(),
                        this.newAssignmentFile
                );
            }
        });
        createAssignmentPanel.add(createButton);
        return createAssignmentPanel;
    }
}
