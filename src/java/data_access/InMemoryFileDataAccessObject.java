package data_access;

import com.dropbox.core.DbxException;
import entities.PDFFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryFileDataAccessObject implements FileDataAccessInterface {
    private Map<String, PDFFile> files = new HashMap<>();

    @Override
    public boolean fileExistsByPath(String path) {
        return false;
    }

    public void saveFile(PDFFile file){
        files.put(file.getFileName(), file);
    }

    public PDFFile getFile(String path){
        return files.get(path);
    }

    public List<PDFFile> getFiles(String path){return null;}
}
