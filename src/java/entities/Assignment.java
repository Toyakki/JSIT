package entities;

public class Assignment {
    private String name;
    private String dueDate;
    private String marks;
    private String stage;
    private String marksReceivedStatus;

    public Assignment(String name, String dueDate, String marks, String stage, String marksReceivedStatus) {
        this.name = name;
        this.dueDate = dueDate;
        this.marks = marks;
        // stage is one of assigned, submitted, graded
        this.stage = stage;
        this.marksReceivedStatus = marksReceivedStatus;
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
}
