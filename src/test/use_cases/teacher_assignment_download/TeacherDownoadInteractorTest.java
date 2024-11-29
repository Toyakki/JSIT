package use_cases.teacher_assignment_download;

import com.dropbox.core.DbxException;
import data_access.InMemoryFileDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import entities.Account;
import entities.Assignment;
import entities.Course;
import entities.PDFFile;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TeacherDownoadInteractorTest {
    private InMemoryUserDataAccessObject userDsGateway;
    private InMemoryFileDataAccessObject fileDsGateway;
    private TeacherDownloadInteractor teacherDownloadInteractor;

    @Before
    void create() {
        userDsGateway = new InMemoryUserDataAccessObject();
        fileDsGateway = new InMemoryFileDataAccessObject();
        TeacherDownloadOutputBoundary outputBoundary = new MockDownloadPresenter();
        teacherDownloadInteractor = new TeacherDownloadInteractor(fileDsGateway, outputBoundary);
    }

    private static class MockDownloadPresenter implements TeacherDownloadOutputBoundary {
        @Override
        public void presentSuccess(String message, PDFFile pdfFile) {
            System.out.println("SUCCESS: " + message);
        }

        @Override
        public void presentError(String message) {
            System.err.println("Error: " + message);
        }
    }

    @Test
    public void testValidDownload() throws DbxException {
        Account teacher_test = userDsGateway.getUserByEmail("t");
        Account tohya_account = userDsGateway.getUserByEmail("henrik-ibsen707@gmail.com");
        String csCourseName = tohya_account.getCourseNames().get(1);
        teacherDownloadInteractor.downloadWrittenAssignment(
                csCourseName,
                tohya_account.getEmail()
        );
    }

    @Test
    public void testInvalidCourseName() throws DbxException {
    }

    @Test
    public void testEmptyFile() throws DbxException {}

    @Test
    public void testInvalidPathName() throws DbxException {}

    @Test
    public void testPathExists() throws DbxException {}

}
