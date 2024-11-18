package view;

import entities.Course;
import interface_adapters.Download.DownloadController;
import interface_adapters.StudentCourse.StudentCourseBackController;
import interface_adapters.StudentCourse.StudentCourseViewModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentCourseView extends JPanel {
    private final StudentCourseViewModel studentCourseViewModel;
    private StudentCourseBackController studentCourseBackController;
    private DownloadController downloadController;

    public StudentCourseView(StudentCourseViewModel viewModel, StudentCourseBackController studentCourseBackController,
                             DownloadController downloadController) {
        this.studentCourseViewModel = viewModel;
        this.studentCourseBackController = studentCourseBackController;
        this.downloadController = downloadController;


        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new JLabel(studentCourseViewModel.getState().getCourseName()));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            studentCourseBackController.back();
        });
        this.add(backButton);

        for (int i = 0; i < studentCourseViewModel.getState().getAssignmentsNames().size(); i++) {
            add(new JLabel(studentCourseViewModel.getState().getAssignmentsNames().get(i)));
            add(new JLabel(studentCourseViewModel.getState().getAssignmentsDueDates().get(i)));
            add(new JLabel(studentCourseViewModel.getState().getAssignmentsMarks().get(i)));

            if (studentCourseViewModel.getState().getAssignmentsStages.get(i).equals("graded")){
                JPanel viewAssignmentsPanel = new JPanel();
                viewAssignmentsPanel.setLayout(new BoxLayout(viewAssignmentsPanel, BoxLayout.X_AXIS));
                JButton downloadOriginalButton = new JButton("Download Original");
                JButton downloadSubmittedButton = new JButton("Download Submitted");
                JButton downloadGradedButton = new JButton("Download Graded");

                downloadOriginalButton.addActionListener(e -> {
                    if (e.getSource().equals(downloadOriginalButton)){
                        downloadController.download(studentCourseViewModel.getState().getCourseName(), i);
                    }
                });
                downloadSubmittedButton.addActionListener(e -> {
                    if (e.getSource().equals(downloadSubmittedButton)){
                        downloadController.download(studentCourseViewModel.getState().getCourseName(), "submitted", i);
                    }
                });
                downloadGradedButton.addActionListener(e -> {
                    if (e.getSource().equals(downloadGradedButton)){
                        downloadController.download(studentCourseViewModel.getState().getCourseName(), "graded", i);
                    }
                });

                viewAssignmentsPanel.add(downloadOriginalButton);
                viewAssignmentsPanel.add(downloadSubmittedButton);
                viewAssignmentsPanel.add(downloadGradedButton);
                viewAssignmentsPanel.add(new JLabel(studentCourseViewModel.getState().getAssignmentsMarksRecived().get(i)));
                add(viewAssignmentsPanel);
            }

            else {
                JPanel viewAssignmentsPanel = new JPanel();
                viewAssignmentsPanel.setLayout(new BoxLayout(viewAssignmentsPanel, BoxLayout.X_AXIS));
                JButton downloadButton = new JButton("Download");
                JButton submitButton = new JButton("Submit");

                downloadButton.addActionListener(e -> {
                    if (e.getSource().equals(downloadButton)){
                        downloadController.download(studentCourseViewModel.getState().getCourseName(), i);
                    }
                });
                submitButton.addActionListener(e -> {
                    if (e.getSource().equals(submitButton)){
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setAcceptAllFileFilterUsed(false);
                        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only PDFs", "pdf");
                        fileChooser.addChoosableFileFilter(restrict);


                        fileChooser.showDialog(null, "Upload");
                        // I am stuck

                    }
                });

                viewAssignmentsPanel.add(downloadButton);
                viewAssignmentsPanel.add(submitButton);
                add(viewAssignmentsPanel);
            }
        }

    }
}
