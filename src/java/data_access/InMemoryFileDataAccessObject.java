package data_access;

import entities.PDFFile;

import java.util.List;

public class InMemoryFileDataAccessObject implements FileDataAccessInterface {
    public void saveFile(PDFFile file){}
    public PDFFile getFile(String path){return null;}
    public List<PDFFile> getFiles(String path){return null;}
}
