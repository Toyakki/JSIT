package view;

import interface_adapters.download.DownloadController;
import interface_adapters.student_course.StudentCourseBackController;
import interface_adapters.student_course.StudentCourseViewModel;
import interface_adapters.submit_assignment.SubmitAssignmentController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public class StudentCourseView extends JPanel implements PropertyChangeListener {
    private final String viewName = "student course";
    private final StudentCourseViewModel studentCourseViewModel;
    private StudentCourseBackController studentCourseBackController;
    private DownloadController downloadController;
    private SubmitAssignmentController uploadController;

    private Map<String, JPanel> assignmentNameToPanel = new HashMap<>();

    public StudentCourseView(StudentCourseViewModel viewModel, StudentCourseBackController studentCourseBackController,
                             DownloadController downloadController, SubmitAssignmentController uploadController) {
        this.studentCourseViewModel = viewModel;
        this.studentCourseViewModel.addPropertyChangeListener(this);
        this.studentCourseBackController = studentCourseBackController;
        this.downloadController = downloadController;
        this.uploadController = uploadController;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new JLabel(studentCourseViewModel.getState().getCourseName()));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            studentCourseBackController.back(studentCourseViewModel.getState().getEmail());
        });
        this.add(backButton);
        renderAssignments();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")){
            renderAssignments();
        }
    }

    private void clearScreen() {
        if (!assignmentNameToPanel.isEmpty()) {
            for (String assignmentName : assignmentNameToPanel.keySet()) {
                remove(assignmentNameToPanel.get(assignmentName));
            }
        }
        assignmentNameToPanel.clear();
    }

    private void renderAssignments(){
        clearScreen();
        for (int i = 0; i < studentCourseViewModel.getState().getAssignmentsNames().size(); i++) {
            final int index = i;
            JPanel coursePanel = new JPanel();
            coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
            coursePanel.add(new JLabel(studentCourseViewModel.getState().getAssignmentsNames().get(index)));
            coursePanel.add(new JLabel(studentCourseViewModel.getState().getAssignmentsDueDates().get(index)));
            coursePanel.add(new JLabel(studentCourseViewModel.getState().getAssignmentsMarks().get(index)));

            if (studentCourseViewModel.getState().getAssignmentsStages().get(index).equals("graded")){
                renderGradedAssignmentComponents(index, coursePanel);
            } else {
                renderUngradedAssignmentComponents(index, coursePanel);
            }
            assignmentNameToPanel.put(studentCourseViewModel.getState().getAssignmentsNames().get(index), coursePanel);
            add(coursePanel);
        }
    }

    private void renderGradedAssignmentComponents(int index, JPanel coursePanel){
        JPanel viewAssignmentsPanel = new JPanel();
        viewAssignmentsPanel.setLayout(new BoxLayout(viewAssignmentsPanel, BoxLayout.X_AXIS));
        JButton downloadOriginalButton = new JButton("Download Original");
        JButton downloadSubmittedButton = new JButton("Download Submitted");
        JButton downloadGradedButton = new JButton("Download Graded");

        downloadOriginalButton.addActionListener(e -> {
            if (e.getSource().equals(downloadOriginalButton)){
                downloadController.handleDownloadOriginal(
                        studentCourseViewModel.getState().getCourseName(),
                        studentCourseViewModel.getState().getAssignmentsNames().get(index)
                );
            }
        });
        downloadSubmittedButton.addActionListener(e -> {
            if (e.getSource().equals(downloadSubmittedButton)){
                downloadController.handleTeacherSubmitted(
                        studentCourseViewModel.getState().getCourseName(),
                        studentCourseViewModel.getState().getAssignmentsNames().get(index),
                        studentCourseViewModel.getState().getEmail()
                );
            }
        });
        downloadGradedButton.addActionListener(e -> {
            if (e.getSource().equals(downloadGradedButton)){
                downloadController.handleDownloadFeedback(
                        studentCourseViewModel.getState().getCourseName(),
                        studentCourseViewModel.getState().getAssignmentsNames().get(index),
                        studentCourseViewModel.getState().getEmail()
                );
            }
        });

        viewAssignmentsPanel.add(downloadOriginalButton);
        viewAssignmentsPanel.add(downloadSubmittedButton);
        viewAssignmentsPanel.add(downloadGradedButton);
        viewAssignmentsPanel.add(new JLabel(String.valueOf(studentCourseViewModel.getState().getAssignmentsMarksReceived().get(index))));
        coursePanel.add(viewAssignmentsPanel);
    }

    private void renderUngradedAssignmentComponents(int index, JPanel coursePanel){
        JPanel viewAssignmentsPanel = new JPanel();
        viewAssignmentsPanel.setLayout(new BoxLayout(viewAssignmentsPanel, BoxLayout.X_AXIS));
        JButton downloadButton = new JButton("Download");
        JButton submitButton = new JButton("Submit");

        downloadButton.addActionListener(e -> {
            if (e.getSource().equals(downloadButton)){
                downloadController.handleDownloadOriginal(
                        studentCourseViewModel.getState().getCourseName(),
                        studentCourseViewModel.getState().getAssignmentsNames().get(index)
                );
            }
        });
        submitButton.addActionListener(e -> {
            if (e.getSource().equals(submitButton)){
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only PDFs", "pdf");
                fileChooser.addChoosableFileFilter(restrict);
                int fileSelectionStatus = fileChooser.showDialog(null, "Upload");
                if (fileSelectionStatus == JFileChooser.APPROVE_OPTION) {
                    uploadController.handleSubmitAssignment(
                            fileChooser.getSelectedFile(),
                            studentCourseViewModel.getState().getCourseName(),
                            studentCourseViewModel.getState().getAssignmentsNames().get(index),
                            studentCourseViewModel.getState().getEmail());
                }
            }
        });

        viewAssignmentsPanel.add(downloadButton);
        viewAssignmentsPanel.add(submitButton);
        coursePanel.add(viewAssignmentsPanel);
    }

    public String getViewName(){
        return this.viewName;
    }
}
