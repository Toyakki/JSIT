package data_access;

import com.dropbox.core.v2.files.*;
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
        try (InputStream in = new ByteArrayInputStream(file.getContent())) {
            FileMetadata metadata = client.files()
                    .uploadBuilder(file.getPath())
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(in);
            System.out.println("File uploaded successfully to:" + metadata.getPathLower());
        } catch (DbxException | IOException e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage(), e);
        }
    }

    @Override
    public PDFFile getFile(String path) {
        // Download the file from Dropbox
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            DownloadBuilder downloader = client.files().downloadBuilder(path);
            downloader.download(out);
            byte[] content = out.toByteArray();

            // Return the PDFFile entity
            return new PDFFile(new File(path).getName(), path, content);
        } catch (IOException | DbxException e) {
            throw new RuntimeException("Error downloading file: " + e.getMessage(), e);
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

    // Testing
    public static void main(String[] args) {
        // Test the DropBoxDataAccessObject functionality
        DropBoxDataAccessObject dao = new DropBoxDataAccessObject();

        // Test uploading a file
        String testFilePath = "CSC258_Project_DrMario.pdf";
        try {
            // Create a test file
            String testContent = "This is a test file for Dropbox upload.";
            byte[] content = testContent.getBytes();
            PDFFile testFile = new PDFFile(testFilePath, "/" + testFilePath, content);

            System.out.println("Uploading file...");
            dao.saveFile(testFile);

            // Test downloading the same file
            System.out.println("Downloading file...");
            PDFFile downloadedFile = dao.getFile("/" + testFilePath);

            // Print the content of the downloaded file
            System.out.println("Downloaded File Content: " + new String(downloadedFile.getContent()));

        } catch (Exception e) {
            e.printStackTrace();
        }
}
}
