package interface_adapters.teacher;

import interface_adapters.ViewModel;

public class TeacherClassesViewModel extends ViewModel<TeacherClassesState> {
    public TeacherClassesViewModel() {
        super("teacher classes");
        setState(new TeacherClassesState());
    }
}
