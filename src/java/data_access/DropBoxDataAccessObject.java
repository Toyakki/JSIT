package data_access;

import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.sharing.ListFoldersBuilder;
import entities.Account;
import entities.Assignment;
import entities.Course;
import entities.PDFFile;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;
import com.dropbox.core.DbxDownloader;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.*;
import java.nio.file.Files;
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
        // Get the local Downloads folder path
        String downloadsFolderPath = System.getProperty("user.home") + File.separator + "Downloads";

        // Extract the file name from the Dropbox path
        String fileName = new File(path).getName();

        // Create the full local file path in the Downloads folder
        File localFile = new File(downloadsFolderPath, fileName);

        try (OutputStream out = new FileOutputStream(localFile)) {

            // Download the file content from Dropbox and save it to the Downloads folder
            client.files().downloadBuilder(path).download(out);

            System.out.println("File downloaded to: " + localFile.getAbsolutePath());

            return new PDFFile(fileName, path, Files.readAllBytes(localFile.toPath()));
        } catch (DbxException | IOException e) {
            throw new RuntimeException("Error in downloading the file from Dropbox: " + e.getMessage(), e);
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

            // Get the local Downloads folder path
            String downloadsFolderPath = System.getProperty("user.home") + File.separator + "Downloads";

            for (Metadata metadata : result.getEntries()) {
                // Process only file entries
                if (metadata instanceof FileMetadata fileMetadata) {
                    String fileName = fileMetadata.getName();

                    // Create a File object for the target location in Downloads
                    File localFile = new File(downloadsFolderPath, fileName);

                    // Download the file content to the Downloads folder
                    try (OutputStream out = new FileOutputStream(localFile)) {
                        client.files().download(fileMetadata.getPathLower()).download(out);
                    }

                    System.out.println("File downloaded to: " + localFile.getAbsolutePath());

                    // Add the file to the list of PDFFile objects
                    files.add(new PDFFile(
                            fileName,
                            fileMetadata.getPathLower(),
                            Files.readAllBytes(localFile.toPath())
                    ));
                }
            }
        } catch (DbxException | IOException e) {
            throw new RuntimeException("Error obtaining files from Dropbox folder: " + path, e);
        }
        return files;
    }


    @Override
    public void saveUser(Account account) {
        try {
            for (String courseName : account.getCourseNames()) {
                // Define the folder path and file path
                System.out.println(courseName);
                System.out.println(account.getEmail());
                String userFolderPath = "/" + courseName + "/" + account.getEmail();
                String userInfoFilePath = userFolderPath + "/info.txt";

                try {
                    // Try to list the folder
                    client.files().getMetadata(userFolderPath);
                } catch (DbxException e) {
                    try {
                        // If the folder doesn't exist, create it
                        client.files().createFolderV2(userFolderPath);
                        System.out.println("Folder created: " + userFolderPath);
                    } catch (DbxException ex) {
                        throw new RuntimeException("Error creating folder: " + userFolderPath, ex);
                    }
                }

                // Build user information content
                StringBuilder builder = new StringBuilder();
                builder.append("Email: ").append(account.getEmail()).append("\n")
                        .append("Password: ").append(account.getPassword()).append("\n")
                        .append("Type: ").append(account.getType()).append("\n")
                        .append("Courses: ");

                // Append course details
                for (Course course : account.getCourses()) {
                    builder.append("(")
                            .append(course.getInstructor()).append(" | ")
                            .append(course.getName()).append(" | ")
                            .append(course.getCourseCode()).append(" | ")
                            .append(course.getStudentEmails()).append(" | ");

                    // Append all assignment information
                    for (Assignment assignment : course.getAssignments()) {
                        builder.append("(")
                                .append(assignment.getClass()).append(" | ")
                                .append(assignment.getName()).append(" | ")
                                .append(assignment.getDueDate()).append(" | ")
                                .append(assignment.getMarks()).append(" | ")
                                .append(assignment.getStage()).append(" | ")
                                .append(assignment.getMarksReceivedStatus()).append(" | ")
                                .append(assignment.getSubmission(account.getEmail())).append(" | ")
                                .append(")");
                    }
                    builder.append("), ");
                }

                // Remove trailing comma if present
                if (builder.toString().endsWith(", ")) {
                    builder.setLength(builder.length() - 2);
                }

                String userInfoContent = builder.toString();

                // Upload the user's info to Dropbox
                InputStream in = new ByteArrayInputStream(userInfoContent.getBytes());
                client.files().uploadBuilder(userInfoFilePath)
                        .withMode(WriteMode.OVERWRITE)
                        .uploadAndFinish(in);

                System.out.println("User information saved at: " + userInfoFilePath);
            }
        } catch (IOException | DbxException e) {
            throw new RuntimeException("Error in saving user: " + e.getMessage(), e);
        }
    }

    // Helper method to convert a serialized list back into a Java List.
    private List<String> parseList(String serializedList) {
        serializedList = serializedList.substring(1, serializedList.length() - 1); // Remove surrounding brackets
        String[] items = serializedList.split(", ");
        return new ArrayList<>(List.of(items));
    }

    private List<Assignment> parseAssignments(String serializedAssignments) {
        // Similar parsing logic as `parseList`, adapted for `Assignment`
        // Implement based on how `Assignment` is serialized
        return new ArrayList<>(); // Replace with actual parsing logic
    }

    @Override
    public Account getUserByEmail(String email) {
        try {

            DbxUserListFolderBuilder coursesFolder = client.files().listFolderBuilder("/");

            for (Metadata courseMetadata: coursesFolder.start().getEntries()){
                // How metadata should be stored for each line:
                // (InstructorName | CourseName | CourseCode | [StudentEmails] | [Assignments])
                if (courseMetadata instanceof FileMetadata){
                    String courseName = courseMetadata.getName();
                    String userInfoFilePath = "/" + courseName + "/" + email;

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    client.files().downloadBuilder(userInfoFilePath).download(out);

                    String userInfoContent = out.toString();

                    // Deserialize
                    String[] lines = userInfoContent.split("\n");
                    String password = lines[1].split(": ")[1];
                    String type = lines[2].split(": ")[1];

                    // Parse courses...
                    List<Course> courses = new ArrayList<>();
                    if (lines.length > 3 && lines[3].startsWith("Courses: ")) {
                        String[] courseEntries = lines[3].substring(9).split("\\), ");
                        for (String courseEntry : courseEntries) {
                            if (courseEntry.endsWith(")")) {
                                courseEntry = courseEntry.substring(0, courseEntry.length() - 1);
                            }
                            String[] courseDetails = courseEntry.split(" \\| ");
                            String instructor = courseDetails[0];
                            String className = courseDetails[1];
                            String courseCode = courseDetails[2];
                            List<String> studentEmails = parseList(courseDetails[3]);
                            List<Assignment> assignments = parseAssignments(courseDetails[4]);

                            courses.add(new Course(instructor, className, courseCode, studentEmails, assignments));
                        }
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
        return null;
    }

    @Override
    public boolean userExistsByEmail(String email) {
        try {
            // List all course folders in the root directory
            ListFolderResult courseFolders = client.files().listFolder("");

            for (Metadata metadata : courseFolders.getEntries()) {
                // Check if the metadata represents a folder (course folder)
                if (metadata instanceof FolderMetadata courseFolder) {
                    String courseName = courseFolder.getName();
                    String userFolderPath = "/" + courseName + "/" + email;

                    try {
                        // Check if the user's folder exists in this course
                        System.out.println(userFolderPath);
                        client.files().getMetadata(userFolderPath);
                        return true;
                    } catch (DbxException ignored) {
                        // If the folder doesn't exist, continue searching
                    }
                }
            }
            // User not found in any course folder
            return false;
        } catch (DbxException e) {
            throw new RuntimeException("Error checking user existence: " + e.getMessage(), e);
        }
    }
}