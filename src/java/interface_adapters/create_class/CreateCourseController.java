package interface_adapters.create_class;

import use_cases.create_course.CreateCourseUseCaseInteractor;

public class CreateCourseController {
    private CreateCourseUseCaseInteractor interactor;
    public CreateCourseController(CreateCourseUseCaseInteractor interactor) {
        this.interactor = interactor;
    }
    public void createCourse(String email, String classname) {
        this.interactor.createCourse(email, classname);
    }
}
