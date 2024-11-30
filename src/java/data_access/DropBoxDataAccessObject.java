package data_access;

import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.sharing.ListFoldersBuilder;
import entities.Account;
import entities.PDFFile;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;
import com.dropbox.core.DbxDownloader;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.*;
import java.util.List;

public class DropBoxDataAccessObject implements UserDataAccessInterface, FileDataAccessInterface {
    private static final String ACCESS_TOKEN;

    static {
        Dotenv dotenv = Dotenv.load();
        ACCESS_TOKEN = dotenv.get("ACCESS_TOKEN");
    }

    DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
    DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

    @Override
    public void saveFile(PDFFile file) {
//        Input: filePath.
        // Upload the file to Dropbox
        try (InputStream in = new ByteArrayInputStream(file.getFileContent())) {
            FileMetadata metadata = client.files()
                    .uploadBuilder(file.getFilePath())
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(in);
            System.out.println("File uploaded successfully to:" + metadata.getPathLower());
        } catch (DbxException | IOException e) {
            throw new RuntimeException("Error in uploading file to dropbox", e);
        }
    }

    @Override
    public PDFFile getFile(String path) {
        // Download the file from Dropbox
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            DownloadBuilder downloader = client.files().downloadBuilder(path);
            downloader.download(out);
            byte[] content = out.toByteArray();
            return new PDFFile(new File(path).getName(), path, content);
        } catch (DbxException | IOException e) {
            throw new RuntimeException("Error in downloading the file from dropbox: " + e.getMessage());
        }
    }

    @Override
    public boolean fileExistsByPath(String path) throws DbxException {
        try{
            client.files().listFolderBuilder(path).start();
            return true;
        } catch (DbxException e) {
            throw new RuntimeException("The path does not exist: " + e.getMessage());
        }
    }

    @Override
    public List<PDFFile> getFiles(String path){
        // need to update somehow
        return null;
    }


    @Override
    public void saveUser(Account account) {

    }

    @Override
    public Account getUserByEmail(String email) {
        return null;
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return false;
    }

    @Override
    public List<Account> getAllUsers() {
        return null;
    }
}
