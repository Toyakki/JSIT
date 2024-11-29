package entities;

// Entity to interact with dropbox API.

public class PDFFile {
    private String fileName;
    private String filePath;
    private byte[] fileContent;

    public PDFFile(String fileName, String filePath, byte[] fileContent) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] newContent) {
        this.fileContent = newContent;
    }

}