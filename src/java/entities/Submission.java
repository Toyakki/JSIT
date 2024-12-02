package entities;

public class Submission {
    private PDFFile submittedFile;
    private PDFFile feedbackFile = null;
    private String grade = "ungraded";
    private String stage = "not submitted";

    public Submission(PDFFile submittedFile) {
        this.submittedFile = submittedFile;
    }

    public Submission(){ this.submittedFile = null; }

    public String getStage(){
        return stage;
    }

    public void setStage(String stage){
        this.stage = stage;
    }

    public PDFFile getSubmittedFile() {
        return submittedFile;
    }

    public void setSubmittedFile(PDFFile submittedFile) {
        this.submittedFile = submittedFile;
    }

    public PDFFile getFeedbackFile() {
        return feedbackFile;
    }

    public void setFeedbackFile(PDFFile feedbackFile) {
        this.feedbackFile = feedbackFile;
    }

    public boolean isGraded(){
        return !grade.equals("ungraded");
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
        this.stage = "graded";
    }
}
