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

    public StudentCourseView(StudentCourseViewModel viewModel) {
        this.studentCourseViewModel = viewModel;
        this.courseName = studentCourseViewModel.getState().getCourseName();
        this.assignmentsNames = new ArrayList<>(viewModel.getAssignmentsNames());
        this.assignmentsDueDates = new ArrayList<>(viewModel.getAssignmentsDueDates());
        this.assignmentsStages = new ArrayList<>(viewModel.getAssignmentsStages());
        this.assignmentsMarks = new ArrayList<>(viewModel.getAssignmentsMarks());

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel(courseName));
        for (int i = 0; i < assignmentsNames.size(); i++) {
            add(new JLabel(assignmentsNames.get(i)));
            add(new JLabel(assignmentsDueDates.get(i)));
            add(new JLabel(assignmentsMarks.get(i)));
        }

    }
}
