package data_access;


import entities.PDFFile;


public interface FileDataAccessInterface {
        void saveFile(PDFFile file);
        PDFFile getFile(String path);
    }

