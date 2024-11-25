package data_access;


import entities.PDFFile;

import java.util.List;


public interface FileDataAccessInterface {
        void saveFile(PDFFile file);
        PDFFile getFile(String path);
        List<PDFFile> getFiles(String path);
    }

