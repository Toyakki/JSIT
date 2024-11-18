package view;

import interface_adapters.AssignmentCreater.AssignmentCreaterController;
import interface_adapters.Download.DownloadController;
import interface_adapters.Grades.GradeController;
import interface_adapters.TeacherCourse.TeacherCourseBackController;
import interface_adapters.TeacherCourse.TeacherCourseViewModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TeacherCourseView extends JPanel {
    private final TeacherCourseViewModel teacherCourseViewModel;
    private final String[] columnNames = {"email", "download", "feedback", "submitted", "grade"};
    private TeacherCourseBackController teacherCourseBackController;
    private AssignmentCreaterController assignmentCreaterController;
    private DownloadController downloadController;
    private GradeController gradeController;

    public TeacherCourseView(TeacherCourseViewModel viewModel, TeacherCourseBackController teacherCourseBackController,
                             AssignmentCreaterController assignmentCreaterController, DownloadController downloadController,
                             GradeController gradeController) {
        this.teacherCourseViewModel = viewModel;
        this.teacherCourseBackController = teacherCourseBackController;
        this.assignmentCreaterController = assignmentCreaterController;
        this.downloadController = downloadController;
        this.gradeController = gradeController;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // need to add back button
        this.add(new JLabel(teacherCourseViewModel.getState().getCourseName()));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            teacherCourseBackController.back();
        });
        this.add(backButton);

        this.add(new JLabel("Create Assignment"));
        JButton uploadButton = new JButton("Upload");
        uploadButton.addActionListener(e -> {
            // help
        });
        this.add(uploadButton);

        // need to research date picker

        JTextField totalGradeField = new JTextField("Total Grade");

        this.add(totalGradeField);

        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> {
            assignmentCreaterController.setTotalGrade(totalGradeField.getText());
            // this right?
        })



        for (int i = 0; i < teacherCourseViewModel.getState().getAssignmentsNames().size(); i++) {
            add(new JLabel(teacherCourseViewModel.getState().getAssignmentsNames().get(i)));
            add(new JLabel(teacherCourseViewModel.getState().assignmentsDueDates().get(i)));
            add(new JLabel(teacherCourseViewModel.getState().assignmentsMarks().get(i)));

            JTable assignmentsTable = new JTable(teacherCourseViewModel.getState().getStudentEmails().size(), 4);
            for (int x = 0; x < 5; x++){
                assignmentsTable.setValueAt(columnNames[x], x, 0);
            }

            // how can see what this looks like

            for (int x = 0; x < teacherCourseViewModel.getState().getStudentEmails().size(); x++){
                assignmentsTable.setValueAt(teacherCourseViewModel.getState().getStudentEmails().get(x), 0, x + 1);

                if (!teacherCourseViewModel.getState().getAssignmentsStages.get(i).equals("assigned")){
                    JButton downloadButton = new JButton("Download");
                    assignmentsTable.setValueAt(downloadButton, 1, x + 1);
                    downloadButton.addActionListener(e -> {
                        downloadController.download(teacherCourseViewModel.getState().getCourseName(), teacherCourseViewModel.getState().getStudentEmails().get(x), "submitted", i);
                    });
                }

                if (teacherCourseViewModel.getState().getAssignmentsStages.get(i).equals("graded")){
                    JButton gradingButton = new JButton("graded/download");
                    assignmentsTable.setValueAt(gradingButton, 2, x + 1);
                    gradingButton.addActionListener(e -> {
                        downloadController.download(teacherCourseViewModel.getState().getCourseName(), teacherCourseViewModel.getState().getStudentEmails().get(x), "graded", i);
                    });
                }
                else {
                    JButton gradingButton = new JButton("not graded/upload");
                    assignmentsTable.setValueAt(gradingButton, 2, x + 1);
                    gradingButton.addActionListener(e -> {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setAcceptAllFileFilterUsed(false);
                        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only PDFs", "pdf");
                        fileChooser.addChoosableFileFilter(restrict);


                        fileChooser.showDialog(null, "Upload");
                        // I am stuck

                    });
                }

                if (teacherCourseViewModel.getState().getAssignmentsStages.get(i).equals("assigned")){
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

        add(assignmentsTable);

        }

    }
}
