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
import java.util.*;


public class DropBoxDataAccessObject implements UserDataAccessInterface, FileDataAccessInterface {
    private static final String ACCESS_TOKEN;
    private static final String DATA_FILE_PATH = "/university_data.txt";

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

            // Read existing data from Dropbox.
            StringBuilder existingData = new StringBuilder();
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                client.files().downloadBuilder(DATA_FILE_PATH).download(out);
                existingData.append(out.toString());
            } catch (DbxException e) {
                // File might not exist initially; we'll create it later
            }

            // Parse existing data into registries
            Map<String, Account> existingUsers = new HashMap<>();
            Map<String, Course> existingCourses = new HashMap<>();
            Map<String, List<Assignment>> courseAssignments = new HashMap<>();
            parseData(existingData.toString(), existingUsers, existingCourses, courseAssignments);

            // Add or update the user, courses, and assignments
            existingUsers.put(account.getEmail(), account);
            for (Course course : account.getCourses()) {
                existingCourses.putIfAbsent(course.getName(), course);

                // Add or update assignments for this course
                if (!courseAssignments.containsKey(course.getName())) {
                    courseAssignments.put(course.getName(), new ArrayList<>());
                }
                for (Assignment assignment : course.getAssignments()) {
                    if (!courseAssignments.get(course.getName()).contains(assignment)) {
                        courseAssignments.get(course.getName()).add(assignment);
                    }
                }
            }

            // Serialize updated data
            StringBuilder builder = new StringBuilder();

            // Serialize USERS
            builder.append("USERS:\n");
            for (Account user : existingUsers.values()) {
                builder.append(user.getEmail()).append(" | ")
                        .append(user.getPassword()).append(" | ")
                        .append(user.getType()).append(" | ")
                        .append(user.getCourseNames()).append("\n");
            }

            // Serialize COURSES
            builder.append("\nCOURSES:\n");
            for (Course course : existingCourses.values()) {
                builder.append(course.getName()).append(" | ")
                        .append(course.getInstructor()).append(" | ")
                        .append(course.getStudentEmails()).append(" | \n");
            }

            // Serialize ASSIGNMENTS
            builder.append("\nASSIGNMENTS:\n");
            for (Map.Entry<String, List<Assignment>> entry : courseAssignments.entrySet()) {
                Course course = existingCourses.get(entry.getKey());
                List<Assignment> assignments = entry.getValue();

                for (Assignment assignment : assignments) {
                    builder.append(assignment.getName()).append(" | [")
                            .append(course.getName()).append(" | ")
                            .append(course.getInstructor()).append(" | ")
                            .append(course.getStudentEmails()).append("] | ")
                            .append(assignment.getDueDate()).append(" | ")
                            .append(assignment.getMarks()).append(" | ")
                            .append(assignment.getStage()).append(" | ")
                            .append(assignment.getMarksReceivedStatus()).append(" | [");

                    if (builder.toString().endsWith(", ")) {
                        builder.setLength(builder.length() - 2); // Remove trailing comma
                    }
                    builder.append("]\n");
                }

            String updatedContent = builder.toString();

            // Step 5: Write the updated data back to Dropbox
            InputStream in = new ByteArrayInputStream(updatedContent.getBytes());
            client.files().uploadBuilder(DATA_FILE_PATH)
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(in);

            System.out.println("Data successfully updated in Dropbox at: " + DATA_FILE_PATH);
        }
        } catch (DbxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseData(String data,
                           Map<String, Account> users,
                           Map<String, Course> courses,
                           Map<String, List<Assignment>> courseAssignments) {
        String[] sections = data.split("\n\n");
        for (String section : sections) {
            if (section.startsWith("USERS:")) {
                String[] lines = section.split("\n");
                for (int i = 1; i < lines.length; i++) {
                    String[] parts = lines[i].split(" \\| ");
                    String email = parts[0];
                    String password = parts[1];
                    String type = parts[2];
                    users.put(email, new Account(email, password, type, new ArrayList<>())); // Empty courses for now
                }
            } else if (section.startsWith("COURSES:")) {
                String[] lines = section.split("\n");
                for (int i = 1; i < lines.length; i++) {
                    String[] parts = lines[i].split(" \\| ");
                    String courseName = parts[0].trim();
                    String instructor = parts[1].trim();
                    List<String> studentEmails = parseList(parts[2].trim());
                    courses.put(courseName, new Course(instructor, courseName, courseName, studentEmails, new ArrayList<>()));
                }
            } else if (section.startsWith("ASSIGNMENTS:")) {
                String[] lines = section.split("\n");
                for (int i = 1; i < lines.length; i++) { // Skip header
                    String[] parts = lines[i].split(" \\| ");
                    String assignmentName = parts[0].trim();

                    // Parse Course details from the serialized string
                    String courseData = parts[1].trim(); // Remove surrounding brackets
                    Course course = parseCourse(courseData, courses);

                    // Parse Assignment details
                    String dueDate = parts[2].trim();
                    String marks = parts[3].trim();
                    String stage = parts[4].trim();
                    String marksReceivedStatus = parts[5].trim();

                    // Create Assignment and associate it with the course
                    Assignment assignment = new Assignment(course, assignmentName, dueDate, marks, stage, marksReceivedStatus);
                    courseAssignments.computeIfAbsent(course.getName(), k -> new ArrayList<>()).add(assignment);
                }
            }
        }
    }

    private Course parseCourse(String courseData, Map<String, Course> courses) {
        // Format: CSC207 | Jonahan Calver | [student1@example.com, student2@example.com]
        String[] parts = courseData.split(" \\| ");
        String courseName = parts[0].trim();
        String instructor = parts[1].trim();
        List<String> studentEmails = parseList(parts[2].trim());

        // Check if the course already exists in the registry
        if (!courses.containsKey(courseName)) {
            courses.put(courseName, new Course(instructor, courseName, courseName, studentEmails, new ArrayList<>()));
        }
        return courses.get(courseName);
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
                            null,
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
