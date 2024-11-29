package data_access;


import com.dropbox.core.DbxException;
import entities.PDFFile;

import java.util.List;


public interface FileDataAccessInterface {
        void saveFile(PDFFile file);
        PDFFile getFile(String path);
        boolean fileExistsByPath(String path) throws DbxException;
        List<PDFFile> getFiles(String path);
    }

