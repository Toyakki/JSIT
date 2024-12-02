package data_access;

import com.dropbox.core.DbxException;
import entities.PDFFile;

import java.util.ArrayList;
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
        files.put(file.getFilePath(), file);
    }

    public PDFFile getFile(String path){
        return files.get(path);
    }

    public List<PDFFile> getFiles(String path){
        List<PDFFile> matchingFiles = new ArrayList<>();
        for (PDFFile file : files.values()) {
            if(file.getFilePath().contains(path)){
                matchingFiles.add(file);
            }
        }
        return matchingFiles;
    }

    public List<PDFFile> getAllFiles(){
        return List.copyOf(files.values());
    }
}
