package data_access;

import com.dropbox.core.DbxException;
import entities.PDFFile;

import java.util.List;

public class InMemoryFileDataAccessObject implements FileDataAccessInterface {
    public void saveFile(PDFFile file){}
    public PDFFile getFile(String path){return null;}

    @Override
    public boolean fileExistsByPath(String path){
        return false;
    }

    public List<PDFFile> getFiles(String path){return null;}
}
