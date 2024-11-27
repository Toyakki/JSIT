package view;

import interface_adapters.create_assignment.AssignmentCreaterController;
import interface_adapters.download.DownloadController;
import interface_adapters.grade.GradeController;
import interface_adapters.teacher_course.TeacherCourseBackController;
import interface_adapters.teacher_course.TeacherCourseViewModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class TeacherCourseView extends JPanel {
    private final String viewName = "teacher course";
    private final TeacherCourseViewModel teacherCourseViewModel;
    private final String[] columnNames = {"email", "download", "feedback", "submitted", "grade"};
    private TeacherCourseBackController teacherCourseBackController;
    private AssignmentCreaterController assignmentCreaterController;
    private DownloadController downloadController;
    private GradeController gradeController;
//    private UploadController uploadController;
    private File newAssignmentFile;

    public TeacherCourseView(TeacherCourseViewModel viewModel, TeacherCourseBackController teacherCourseBackController,
                             AssignmentCreaterController assignmentCreaterController, DownloadController downloadController,
                             GradeController gradeController) {
        this.teacherCourseViewModel = viewModel;
        this.teacherCourseBackController = teacherCourseBackController;
        this.assignmentCreaterController = assignmentCreaterController;
        this.downloadController = downloadController;
        this.gradeController = gradeController;
//        this.uploadController = uploadController;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // need to add back button
        this.add(new JLabel(teacherCourseViewModel.getState().getCourseName()));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            teacherCourseBackController.back(teacherCourseViewModel.getState().getEmail());
        });
        this.add(backButton);

        this.add(new JLabel("Create Assignment"));
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
        this.add(uploadButton);

        this.add(new JLabel("Set Due Date, e.g. Jan. 1"));
        JTextField dueDate = new JTextField();
        this.add(dueDate);

        JTextField totalGradeField = new JTextField("Total Grade");
        this.add(totalGradeField);

        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> {
            if (newAssignmentFile.length() == 0) {
                JOptionPane.showMessageDialog(null, "Assignment not Selected");
            }
            else {
                assignmentCreaterController.createAssignment(
                        dueDate.getText(),
                        totalGradeField.getText(),
                        newAssignmentFile
                );
            }
        });



        for (int i = 0; i < teacherCourseViewModel.getState().getAssignmentsNames().size(); i++) {
            final int outer_index = i;
            add(new JLabel(teacherCourseViewModel.getState().getAssignmentsNames().get(i)));
            add(new JLabel(teacherCourseViewModel.getState().getAssignmentsDueDates().get(i)));
            add(new JLabel(teacherCourseViewModel.getState().getAssignmentsMarks().get(i)));

            JTable assignmentsTable = new JTable(teacherCourseViewModel.getState().getStudentEmails().size(), 4);
            for (int x = 0; x < 5; x++){
                assignmentsTable.setValueAt(columnNames[x], x, 0);
            }

            for (int x = 0; x < teacherCourseViewModel.getState().getStudentEmails().size(); x++){
                final int index = x;
                assignmentsTable.setValueAt(teacherCourseViewModel.getState().getStudentEmails().get(x), 0, x + 1);

                if (!teacherCourseViewModel.getState().getAssignmentsStages().get(i).equals("assigned")){
                    JButton downloadButton = new JButton("Download");
                    assignmentsTable.setValueAt(downloadButton, 1, x + 1);
                    final String email = teacherCourseViewModel.getState().getStudentEmails().get(x);
                    downloadButton.addActionListener(e -> {
                        downloadController.download(teacherCourseViewModel.getState().getCourseName(), email, "submitted");
                    });
                }

                if (teacherCourseViewModel.getState().getAssignmentsStages().get(i).equals("graded")){
                    JButton gradingButton = new JButton("graded/download");
                    assignmentsTable.setValueAt(gradingButton, 2, x + 1);
                    final String email = teacherCourseViewModel.getState().getStudentEmails().get(x);
                    gradingButton.addActionListener(e -> {
                        downloadController.download(teacherCourseViewModel.getState().getCourseName(), email, "graded");
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
                        int fileSelectionStatus = fileChooser.showDialog(null, "Upload");
                        if (fileSelectionStatus == JFileChooser.APPROVE_OPTION) {
//                            uploadController.uploadGraded(fileChooser.getSelectedFile(), teacherCourseViewModel.getState().getStudentEmails().get(index), teacherCourseViewModel.getState().getAssignmentsNames());
                        }
                    });
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

        add(assignmentsTable);

        }

    }

    public String getViewName(){
        return this.viewName;
    }
}
