package use_cases.teacher_download;

import com.dropbox.core.DbxException;
import data_access.InMemoryFileDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import entities.Account;
import entities.PDFFile;
import org.junit.jupiter.api.Test;
import use_cases.download.DownloadInteractor;


public class TeacherDownoadInteractorTest {
    private InMemoryUserDataAccessObject userDsGateway;
    private InMemoryFileDataAccessObject fileDsGateway;
    private DownloadInteractor teacherDownloadInteractor;

    void create() {
        userDsGateway = new InMemoryUserDataAccessObject();
        fileDsGateway = new InMemoryFileDataAccessObject();
        teacherDownloadInteractor = new DownloadInteractor(fileDsGateway, userDsGateway);
    }

    @Test
    public void testValidDownload() throws DbxException {
        Account teacher_test = userDsGateway.getUserByEmail("t");
        Account tohya_account = userDsGateway.getUserByEmail("henrik-ibsen707@gmail.com");
        String csCourseName = tohya_account.getCourseNames().get(1);
        teacherDownloadInteractor.downloadWrittenAssignment(
                csCourseName,
                "assignmentName", // need to fix!
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
