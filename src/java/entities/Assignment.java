package entities;

import java.util.HashMap;
import java.util.Map;

public class Assignment {
    private Course course;
    private String name;
    private String dueDate;
    private String marks;
    private String stage;
    private String marksReceivedStatus;
    private Map<String, Submission> submissionMap;

    public Assignment(Course course, String name, String dueDate, String marks, String stage, String marksReceivedStatus) {
        this.course = course;
        this.name = name;
        this.dueDate = dueDate;
        this.marks = marks;
        // stage is one of assigned, submitted, graded
        this.stage = stage;
        this.marksReceivedStatus = marksReceivedStatus;

        this.submissionMap = new HashMap<>();
        for (String student : this.course.getStudentEmails()){
            this.submissionMap.put(student, new Submission());
        }
    }

    public String getName() {
        return name;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getMarks() {
        return marks;
    }

    public String getStage() {
        return stage;
    }

    public String getMarksReceivedStatus() {
        return marksReceivedStatus;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Submission getSubmission(String student) { return submissionMap.get(student); }
}
