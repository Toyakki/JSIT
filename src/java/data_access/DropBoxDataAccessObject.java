package data_access;

import com.dropbox.core.v2.files.*;
import entities.*;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.*;
import java.nio.file.Files;
import java.util.*;


public class DropBoxDataAccessObject implements UserDataAccessInterface, FileDataAccessInterface {
    private static final String ACCESS_TOKEN;
    private static final String DATA_FILE_PATH = "/university_data.txt";

    private static final InMemoryUserDataAccessObject inMemoryUserDataAccessObject = new InMemoryUserDataAccessObject();
    private static final InMemoryFileDataAccessObject inMemoryFileDataAccessObject = new InMemoryFileDataAccessObject();
    private static final Map<String, Account> users = new HashMap<>();
    private static final Map<String, Course> courses = new HashMap<>();
    private static final Map<String, List<Assignment>> courseToAssignments = new HashMap<>();
    private static final Map<String, Map<String, Map<String, Submission>>> courseToAssignmentToSubmissions = new HashMap<>();

    static {
        Dotenv dotenv = Dotenv.load();
        ACCESS_TOKEN = dotenv.get("ACCESS_TOKEN");
    }

    public DropBoxDataAccessObject() {
        StringBuilder existingData = new StringBuilder();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            client.files().downloadBuilder(DATA_FILE_PATH).download(out);
            existingData.append(out.toString());
        } catch (IOException | DbxException e) {
            // File might not exist initially; we'll create it later
            throw new RuntimeException(e);
        }
        parseData(existingData.toString(), users, courses, courseToAssignments, courseToAssignmentToSubmissions);
    }

    DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
    DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

    @Override
    public void saveFile(PDFFile file) {
        try {
            // Ensure the directory structure exists
            String filePath = file.getFilePath();
            String folderPath = filePath.substring(0, filePath.lastIndexOf('/')); // Extract the folder path
            ensureDirectoryExists(folderPath);

            // Upload the file to Dropbox
            try (InputStream in = new ByteArrayInputStream(file.getFileContent())) {
                FileMetadata metadata = client.files()
                        .uploadBuilder(file.getFilePath())
                        .withMode(WriteMode.OVERWRITE)
                        .uploadAndFinish(in);
                System.out.println("File uploaded successfully to: " + metadata.getPathLower());
            }

            // Save the file in the in-memory DAO as well
            inMemoryFileDataAccessObject.saveFile(file);

        } catch (DbxException | IOException e) {
            throw new RuntimeException("Error in uploading file to Dropbox", e);
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
        try {
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
            // Step 1: Read existing data from Dropbox
            StringBuilder existingData = new StringBuilder();
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                client.files().downloadBuilder(DATA_FILE_PATH).download(out);
                existingData.append(out.toString());
            } catch (DbxException e) {
                // File might not exist initially; we'll create it later
            }

            // Step 2: Parse existing data into registries
            Map<String, Account> existingUsers = new HashMap<>();
            Map<String, Course> existingCourses = new HashMap<>();
            Map<String, List<Assignment>> courseAssignments = new HashMap<>();
            Map<String, Map<String, Map<String, Submission>>> courseToAssignmentToSubmission = new HashMap<>();
            parseData(existingData.toString(), existingUsers, existingCourses,
                    courseAssignments, courseToAssignmentToSubmission);

            existingUsers.put(account.getEmail(), account);

            for (Course course : account.getCourses()) {
                // Add or update courses
                existingCourses.putIfAbsent(course.getName(), course);

                // Add or update assignments for this course
                courseAssignments.computeIfAbsent(course.getName(), k -> new ArrayList<>());
                for (Assignment assignment : course.getAssignments()) {
                    if (!courseAssignments.get(course.getName()).contains(assignment)) {
                        courseAssignments.get(course.getName()).add(assignment);
                    }

                    // Add or update submissions for this assignment
                    courseToAssignmentToSubmissions
                            .computeIfAbsent(course.getName(), k -> new HashMap<>())
                            .computeIfAbsent(assignment.getName(), k -> new HashMap<>())
                            .putIfAbsent(account.getEmail(), new Submission());
                }
            }

            // Step 4: Serialize updated data into the required format
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
                        .append(course.getClassCode()).append(" | ")
                        .append(course.getStudentEmails()).append("\n");
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
                            .append(course.getClassCode()).append(" | ")
                            .append(course.getStudentEmails()).append("] | ")
                            .append(assignment.getDueDate()).append(" | ")
                            .append(assignment.getMarks()).append(" | ")
                            .append(assignment.getStage()).append(" | ")
                            .append(assignment.getMarksReceivedStatus()).append("\n");
                }
            }


            // Serialize SUBMISSIONS
            builder.append("\nSUBMISSIONS:\n");
            for (Map.Entry<String, Map<String, Map<String, Submission>>> courseEntry : courseToAssignmentToSubmissions.entrySet()) {
                String courseName = courseEntry.getKey();
                Map<String, Map<String, Submission>> assignmentMap = courseEntry.getValue();

                for (Map.Entry<String, Map<String, Submission>> assignmentEntry : assignmentMap.entrySet()) {
                    String assignmentName = assignmentEntry.getKey();
                    Map<String, Submission> submissionMap = assignmentEntry.getValue();

                    for (Map.Entry<String, Submission> submissionEntry : submissionMap.entrySet()) {
                        String studentEmail = submissionEntry.getKey();
                        Submission submission = submissionEntry.getValue();

                        builder.append("[").append(courseName).append(" | ")
                                .append(assignmentName).append(" | ")
                                .append(studentEmail).append("] | ");

                        if (submission.getFeedbackFile() != null) {
                            builder.append(submission.getFeedbackFile().getFileName()).append(" | ")
                                    .append(submission.getFeedbackFile().getFilePath()).append(" | ");
                        } else {
                            builder.append("null | null | ");
                        }

                        builder.append(submission.getGrade()).append(" | ")
                                .append(submission.getStage()).append("\n");
                    }
                }
            }

            String updatedContent = builder.toString();

            // Write the updated data back to Dropbox
            InputStream in = new ByteArrayInputStream(updatedContent.getBytes());
            client.files().uploadBuilder(DATA_FILE_PATH)
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(in);

            System.out.println("Data successfully updated in Dropbox at: " + DATA_FILE_PATH);
        } catch (DbxException | IOException e) {
            throw new RuntimeException("Error saving user to Dropbox: " + e.getMessage(), e);
        }
    }

    @Override
    public Account getUserByEmail(String email) {
        try {

            StringBuilder data = new StringBuilder();
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                client.files().downloadBuilder(DATA_FILE_PATH).download(out);
                data.append(out.toString());
            }

            Map<String, Account> users = new HashMap<>();
            Map<String, Course> courses = new HashMap<>();
            Map<String, List<Assignment>> courseAssignments = new HashMap<>();
            Map<String, Map<String, Map<String, Submission>>> courseToAssignmentToSubmissions = new HashMap<>();
            parseData(data.toString(), users, courses, courseAssignments, courseToAssignmentToSubmissions);

            if (users.containsKey(email)) {
                Account account = users.get(email);

                List<Course> userCourses = new ArrayList<>();
                for (String courseName : account.getCourseNames()) {
                    Course course = courses.get(courseName);
                    if (course != null) {
                        List<Assignment> assignments = courseAssignments.getOrDefault(courseName, new ArrayList<>());
                        course = new Course(course.getInstructor(), course.getName(), course.getClassCode(),
                                course.getStudentEmails(), assignments);

                        for (Assignment assignment : assignments) {
                            Map<String, Submission> submissions = courseToAssignmentToSubmissions
                                    .getOrDefault(courseName, new HashMap<>())
                                    .getOrDefault(assignment.getName(), new HashMap<>());

                            for (String studentEmail : course.getStudentEmails()) {
                                if (submissions.containsKey(studentEmail)) {
                                    assignment.setSubmission(studentEmail, submissions.get(studentEmail));
                                }
                            }
                        }

                        userCourses.add(course);
                    }
                }

                return new Account(account.getEmail(), account.getPassword(), account.getType(), userCourses);
            } else {
                throw new IllegalArgumentException("User with email " + email + " does not exist.");
            }
        } catch (DbxException | IOException e) {
            throw new RuntimeException("Error retrieving user by email: " + email, e);
        }
    }


    @Override
    public boolean userExistsByEmail(String email) {
        try {
            // Download the current data from Dropbox
            StringBuilder data = new StringBuilder();
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                client.files().downloadBuilder(DATA_FILE_PATH).download(out);
                data.append(out.toString());
            }

            // Parse the university data
            Map<String, Account> users = new HashMap<>();
            Map<String, Course> courses = new HashMap<>();
            Map<String, List<Assignment>> courseAssignments = new HashMap<>();
            Map<String, Map<String, Map<String, Submission>>> courseToAssignmentToSubmissions = new HashMap<>();
            parseData(data.toString(), users, courses, courseAssignments, courseToAssignmentToSubmissions);

            // Check if the email exists in the users map
            return users.containsKey(email);
        } catch (DbxException | IOException e) {
            throw new RuntimeException("Error checking user existence by email: " + email, e);
        }
    }

    @Override
    public List<Account> getAllUsers() {
        return inMemoryUserDataAccessObject.getAllUsers();
    }

    private void parseData(String data,
                           Map<String, Account> users,
                           Map<String, Course> courses,
                           Map<String, List<Assignment>> courseAssignments,
                           Map<String, Map<String, Map<String, Submission>>> courseToAssignmentToSubmissions) {
        String[] sections = data.split("\n\n");
        for (String section : sections) {
            if (section.startsWith("USERS:")) {
                String[] lines = section.split("\n");
                for (int i = 1; i < lines.length; i++) {
                    String[] parts = lines[i].split(" \\| ");
                    String email = parts[0].trim();
                    String password = parts[1].trim();
                    String type = parts[2].trim();
                    List<String> courseNames = parseList(parts[3].trim());
                    users.put(email, new Account(email, password, type, new ArrayList<>()));
                }
            } else if (section.startsWith("COURSES:")) {
                String[] lines = section.split("\n");
                for (int i = 1; i < lines.length; i++) {
                    String[] parts = lines[i].split(" \\| ");
                    String courseName = parts[0].trim();
                    String instructor = parts[1].trim();
                    String classCode = parts[2].trim();
                    List<String> studentEmails = parseList(parts[3].trim());
                    courses.put(courseName, new Course(instructor, courseName, classCode, studentEmails, new ArrayList<>()));
                }
            } else if (section.startsWith("ASSIGNMENTS:")) {
                String[] lines = section.split("\n");
                for (int i = 1; i < lines.length; i++) {
                    String[] parts = lines[i].split(" \\| ");
                    String assignmentName = parts[0].trim();
                    Course course = parseCourse(parts[1].trim().substring(1), parts[2].trim(), parts[3].trim(), parts[4].trim().substring(0, parts[4].length() - 1), courses);
                    String dueDate = parts[5].trim();
                    String marks = parts[6].trim();
                    String stage = parts[7].trim();
                    String marksReceivedStatus = parts[8].trim();
                    Assignment assignment = new Assignment(course, assignmentName, dueDate, marks, stage, marksReceivedStatus);
                    courseAssignments.computeIfAbsent(course.getName(), k -> new ArrayList<>()).add(assignment);
                }
            } else if (section.startsWith("SUBMISSIONS:")) {
                String[] lines = section.split("\n");
                for (int i = 1; i < lines.length; i++) {
                    String[] parts = lines[i].split(" \\| ");
                    String[] submissionInfo = parts[0].trim().replace("[", "").replace("]", "").split(" \\| ");
                    String courseName = submissionInfo[0].trim();
                    String assignmentName = submissionInfo[1].trim();
                    String studentEmail = submissionInfo[2].trim();
                    PDFFile feedbackFile = parts[1].trim().equals("-") ? null :
                            new PDFFile(parts[1].trim(), parts[2].trim(), new byte[0]);
                    String grade = parts[3].trim();
                    String stage = parts[4].trim();
                    Submission submission = new Submission();
                    submission.setFeedbackFile(feedbackFile);
                    submission.setGrade(grade);
                    submission.setStage(stage);
                    courseToAssignmentToSubmissions
                            .computeIfAbsent(courseName, k -> new HashMap<>())
                            .computeIfAbsent(assignmentName, k -> new HashMap<>())
                            .put(studentEmail, submission);
                }
            }
        }
    }

    private Course parseCourse(String courseName, String instructor,
                               String classCode,
                               String studentEmailList,
                               Map<String, Course> courses) {
        // Format: [CourseName, Instructor, ClassCode, [StudentEmails]]

        List<String> studentEmails = parseList(studentEmailList);

        // Check if the course already exists in the registry
        if (!courses.containsKey(courseName)) {
            courses.put(courseName, new Course(instructor, courseName, classCode, studentEmails, new ArrayList<>()));
        }

        return courses.get(courseName);
    }

    private List<String> parseList(String serializedList) {
        serializedList = serializedList.substring(1, serializedList.length() - 1); // Remove surrounding brackets
        String[] items = serializedList.split(", ");
        return new ArrayList<>(List.of(items));
    }

    private void ensureDirectoryExists(String path) {
        try {
            // Attempt to get metadata for the folder
            client.files().getMetadata(path);
            System.out.println("Directory exists: " + path);
        } catch (DbxException e) {
            // If the folder doesn't exist, recursively create parent directories
            int lastSlashIndex = path.lastIndexOf('/');
            if (lastSlashIndex > 0) {
                String parentPath = path.substring(0, lastSlashIndex);
                ensureDirectoryExists(parentPath); // Create parent folders recursively
            }
            try {
                client.files().createFolderV2(path);
                System.out.println("Created directory: " + path);
            } catch (DbxException ex) {
                throw new RuntimeException("Failed to create directory: " + path, ex);
            }
        }
    }
}
