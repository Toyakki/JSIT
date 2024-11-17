package view;

import entities.Course;
import interface_adapters.StudentCourse.StudentCourseViewModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentCourseView extends JPanel {
    private final StudentCourseViewModel studentCourseViewModel;
    private final String courseName;
    private final List<String> assignmentsNames;
    private final List<String> assignmentsDueDates;
    private final List<String> assignmentsStages;
    private final List<String> assignmentsMarks;
    private final List<String> assignmentsMarksRecived;

    public StudentCourseView(StudentCourseViewModel viewModel) {
        this.studentCourseViewModel = viewModel;
        this.courseName = studentCourseViewModel.getState().getCourseName();
        this.assignmentsNames = new ArrayList<>(viewModel.getAssignmentsNames());
        this.assignmentsDueDates = new ArrayList<>(viewModel.getAssignmentsDueDates());
        this.assignmentsStages = new ArrayList<>(viewModel.getAssignmentsStages());
        this.assignmentsMarks = new ArrayList<>(viewModel.getAssignmentsMarks());
        this.assignmentsMarksRecived = new ArrayList<>(viewModel.getAssignmentsMarksRecived());

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel(courseName));
        for (int i = 0; i < assignmentsNames.size(); i++) {
            add(new JLabel(assignmentsNames.get(i)));
            add(new JLabel(assignmentsDueDates.get(i)));
            add(new JLabel(assignmentsMarks.get(i)));

            if (assignmentsStages.equals("graded")){
                JPanel viewAssignmentsPanel = new JPanel();
                viewAssignmentsPanel.setLayout(new BoxLayout(viewAssignmentsPanel, BoxLayout.X_AXIS));
                JButton downloadOriginalButton = new JButton("Download Original");
                JButton downloadSubmittedButton = new JButton("Download Submitted");
                JButton downloadGradedButton = new JButton("Download Graded");

                downloadOriginalButton.addActionListener(e -> {
                    if (e.getSource().equals(downloadOriginalButton)){
                        //todo
                    }
                });
                downloadSubmittedButton.addActionListener(e -> {
                    if (e.getSource().equals(downloadSubmittedButton)){
                        //todo
                    }
                });
                downloadGradedButton.addActionListener(e -> {
                    if (e.getSource().equals(downloadGradedButton)){
                        //todo
                    }
                });

                viewAssignmentsPanel.add(downloadOriginalButton);
                viewAssignmentsPanel.add(downloadSubmittedButton);
                viewAssignmentsPanel.add(downloadGradedButton);
                viewAssignmentsPanel.add(new JLabel(assignmentsMarksRecived.get(i)));
                add(viewAssignmentsPanel);
            }
            else {
                JPanel viewAssignmentsPanel = new JPanel();
                viewAssignmentsPanel.setLayout(new BoxLayout(viewAssignmentsPanel, BoxLayout.X_AXIS));
                JButton downloadButton = new JButton("Download");
                JButton submitButton = new JButton("Submit");

                downloadButton.addActionListener(e -> {
                    if (e.getSource().equals(downloadButton)){
                        //todo
                    }
                });
                submitButton.addActionListener(e -> {
                    if (e.getSource().equals(submitButton)){
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.showDialog(null, "Upload");

                    }
                });

                viewAssignmentsPanel.add(downloadButton);
                viewAssignmentsPanel.add(submitButton);
                add(viewAssignmentsPanel);
            }
        }

    }
}
