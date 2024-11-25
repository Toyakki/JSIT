package entities;

public class PDFFile {
    private String name;
    private String path;
    private byte[] content;
    private String courseId;
    private String grade;

    public PDFFile(String name, String path, byte[] content) {
        this.name = name;
        this.path = path;
        this.content = content;
        this.courseId = null;
        this.grade = null;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


}
