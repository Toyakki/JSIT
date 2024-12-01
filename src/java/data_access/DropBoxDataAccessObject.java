package data_access;

import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.sharing.ListFoldersBuilder;
import entities.Account;
import entities.Course;
import entities.PDFFile;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;
import com.dropbox.core.DbxDownloader;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.*;
import java.util.ArrayList;
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
    public boolean fileExistsByPath(String path) {
        try{
            client.files().listFolderBuilder(path).start();
            return true;
        } catch (DbxException e) {
            throw new RuntimeException("The path does not exist: " + e.getMessage());
        }
    }

    @Override
    public List<PDFFile> getFiles(String path){
        List<PDFFile> files = new ArrayList<>();
        try {

            // List all files and folders in the given Dropbox folder path
            ListFolderResult result = client.files().listFolder(path);

            for (Metadata metadata : result.getEntries()) {
                // Process only file entries
                if (metadata instanceof FileMetadata fileMetadata) {

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    client.files().download(fileMetadata.getPathLower()).download(out);

                    PDFFile pdfFile = new PDFFile(
                            fileMetadata.getName(),
                            fileMetadata.getPathLower(),
                            out.toByteArray()
                    );
                    files.add(pdfFile);
                }
            }
        } catch (DbxException | IOException e) {
            throw new RuntimeException("Error obtaining files from Dropbox folder: " + path, e);
        }
        return files;
    }


    @Override
    public void saveUser(Account account) {
        // Storing the student's information in each course folder
        try {
            for (String courseName: account.getCourseNames()) {

                String userInfoFilePath = "/" + courseName + "/" + account.getEmail();

                StringBuilder serialized = new StringBuilder();

                // Add more information if needed.
                serialized.append("Email: ").append(account.getEmail()).append("\n")
                        .append("Password: ").append(account.getPassword()).append("\n")
                        .append("Type:").append(account.getType()).append("\n")
                        .append("Courss: ").append(String.join(", ", account.getCourseNames())).append("\n");


                String userInfoContent = serialized.toString();

                // Upload or overwrite the file in Dropbox
                InputStream in = new ByteArrayInputStream(userInfoContent.getBytes());
                client.files().uploadBuilder(userInfoFilePath)
                        .withMode(WriteMode.OVERWRITE)
                        .uploadAndFinish(in);
            }
        } catch(IOException | DbxException e){
            throw new RuntimeException("Error in saving user: " + e.getMessage());
        }

    }

    @Override
    public Account getUserByEmail(String email) {
        try {

            DbxUserListFolderBuilder coursesFolder = client.files().listFolderBuilder("/");

            for (Metadata courseMetadata: coursesFolder.start().getEntries()){
                if (courseMetadata instanceof FileMetadata){
                    String courseName = courseMetadata.getName();
                    String userInfoFilePath = "/" + courseName + "/" + email;

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    client.files().downloadBuilder(userInfoFilePath).download(out);

                    String userInfoContent = new String(out.toByteArray());

                    // Deserializing... somehow?
                    String[] lines = userInfoContent.split("\n");
                    String email = lines[0].split(": ")[1];
                    String password = lines[1].split(": ")[1];
                    String type = lines[2].split(": ")[1];
                    List <Course> courses = new ArrayList<>();

                    // TODO: Find the way to store courses. Any methods to retrieve Courses entity from the course names?
                    for (String courseNameEntry : courseNames){
                        Course course = new Course();

                    }
                    return new Account(email, password, type, courses);

                }
            }
        } catch (ListFolderErrorException e) {
            throw new RuntimeException(e);
        } catch (DbxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean userExistsByEmail(String email) {
        try {
            DbxUserListFolderBuilder coursesFolder = client.files().listFolderBuilder("/");

            for (Metadata courseMetadata : coursesFolder.start().getEntries()) {
                if (courseMetadata instanceof FileMetadata) {
                    String courseName = courseMetadata.getName();
                    String userInfoFilePath = "/" + courseName + "/" + email;
                    try {
                        client.files().getMetadata(userInfoFilePath);
                        return true;
                    } catch (DbxException ignored) {
                        // Continue searching
                    }
                }
            }
            return false;
        } catch (DbxException e) {
            throw new RuntimeException("Error obtaining user: " + e.getMessage());
        }
    }
}
