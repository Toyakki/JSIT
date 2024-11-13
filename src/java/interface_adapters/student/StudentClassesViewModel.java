package interface_adapters.student;

import interface_adapters.ViewModel;

public class StudentClassesViewModel extends ViewModel<StudentClassesState> {
    public StudentClassesViewModel() {
        super("student classes");
        setState(new StudentClassesState());
    }
}
