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
    public List<PDFFile> getFiles(String path) {
        List<PDFFile> files = new ArrayList<>();
        try {
            // List all entries (files and folders) in the specified path
            ListFolderResult result = client.files().listFolder(path);

            for (Metadata metadata : result.getEntries()) {
                if (metadata instanceof FileMetadata) {
                    // If it's a file, download it using getFile
                    String filePath = metadata.getPathLower();
                    files.add(getFile(filePath));
                } else if (metadata instanceof FolderMetadata) {
                    // If it's a folder, recursively call getFiles
                    String folderPath = metadata.getPathLower();
                    files.addAll(getFiles(folderPath));
                }
            }
        } catch (DbxException e) {
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
                                .append(assignment.getCourseName()).append(" | ")
                                .append(assignment.getName()).append(" | ")
                                .append(assignment.getDueDate()).append(" | ")
                                .append(assignment.getMarks()).append(" | ")
                                .append(assignment.getStage()).append(" | ")
                                .append(assignment.getMarksReceivedStatus())
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

    @Override
    public Account getUserByEmail(String email) {
        try {
            // List all course folders
            ListFolderResult courseFolders = client.files().listFolderBuilder("").start();

            for (Metadata metadata : courseFolders.getEntries()) {
                if (metadata instanceof FolderMetadata courseFolder) {
                    String courseName = courseFolder.getName();
                    String userInfoFilePath = "/" + courseName + "/" + email + "/info.txt";

                    try {
                        // Download the user's info.txt file
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        client.files().downloadBuilder(userInfoFilePath).download(out);

                        String userInfoContent = out.toString();

                        // Deserialize user info (using a helper function)
                        String[] lines = userInfoContent.split("\n");
                        String password = lines[1].split(": ")[1];
                        String type = lines[2].split(": ")[1];

                        // Parse courses
                        List<Course> courses = new ArrayList<>();
                        if (lines.length > 3 && lines[3].startsWith("Courses: ")) {
                            String[] courseEntries = lines[3].substring(9).split("\\), ");
                            for (String courseEntry : courseEntries) {
                                if (courseEntry.endsWith(")")) {
                                    courseEntry = courseEntry.substring(0, courseEntry.length() - 1);
                                }
                                String[] courseDetails = courseEntry.split(" \\| ");
                                String instructor = courseDetails[0].trim();
                                String className = courseDetails[1].trim();
                                String courseCode = courseDetails[2].trim();
                                List<String> studentEmails = parseList(courseDetails[3]);

                                // Add the assignment if exists.
                                if (courseDetails.length > 4) {
                                    List<Assignment> assignments = parseAssignments(courseDetails[4], email);
                                    courses.add(new Course(instructor, className, courseCode, studentEmails, assignments));
                                } else {
                                    courses.add(new Course(instructor, className, courseCode, studentEmails, null));
                                }

                            }
                        }

                        return new Account(email, password, type, courses);

                    } catch (DbxException ignored) {
                        // Continue searching if the file is not found in this folder
                    }
                }
            }

            throw new IllegalArgumentException("User not found: " + email);
        } catch (DbxException | IOException e) {
            throw new RuntimeException("Error retrieving user by email: " + email, e);
        }
    }

    // Helper method to convert a serialized list back into a Java List.
    private List<String> parseList(String serializedList) {
        serializedList = serializedList.substring(1, serializedList.length() - 1); // Remove surrounding brackets
        String[] items = serializedList.split(", ");
        return new ArrayList<>(List.of(items));
    }

    private List<Assignment> parseAssignments(String serializedAssignments, String email) {
        List<Assignment> assignments = new ArrayList<>();

        if (serializedAssignments.startsWith("[") && serializedAssignments.endsWith("]")) {
            serializedAssignments = serializedAssignments.substring(1, serializedAssignments.length() - 1);
        }

        String[] assignmentEntries = serializedAssignments.split("\\), \\(");

        for (String entry : assignmentEntries) {
            entry = entry.replace("(", "").replace(")", ""); // Remove remaining parentheses
            String[] fields = entry.split("\\|"); // Split by '|'

            if (fields.length >= 7) { // Ensure all fields are present
                try {
                    String courseName = fields[0].trim();
                    String studentName = fields[1].trim();
                    String dueDate = fields[2].trim();
                    String marks = fields[3].trim();
                    String stage = fields[4].trim();
                    String marksReceivedStatus = fields[5].trim();

                    Assignment assignment = new Assignment(
                            courseName,
                            studentName,
                            dueDate,
                            marks,
                            stage,
                            marksReceivedStatus
                    );

                    assignments.add(assignment);
                } catch (Exception e) {
                    throw new RuntimeException("Error parsing assignment: " + entry, e);
                }
            }
        }

        return assignments;
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