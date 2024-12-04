package use_cases.teacher_course_view_interactor;

import data_access.FileDataAccessInterface;
import data_access.InMemoryFileDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.UserDataAccessInterface;
import interface_adapters.teacher.TeacherCourseViewPresenter;
import org.junit.jupiter.api.Test;
import use_cases.LoadDummyData;

public class TeacherCourseViewInteractorTest {

    @Test
    void create(){
        FileDataAccessInterface fileDsGateway = new InMemoryFileDataAccessObject();
        UserDataAccessInterface userDsGateway = new InMemoryUserDataAccessObject();
        LoadDummyData loadDummyData = new LoadDummyData();
        loadDummyData.loadData(userDsGateway);

    }

}
