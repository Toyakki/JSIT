package entities;

public class PDFFile {
    private String name;
    private String path;
    private byte[] content;

    public PDFFile(String name, String path, byte[] content) {
        this.name = name;
        this.path = path;
        this.content = content;
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
}
