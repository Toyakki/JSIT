import data_access.InMemoryUserDataAccessObject;
import entities.Account;
import view.LoginView;

public class Main {
    public static void main(String[] args) {
        InMemoryUserDataAccessObject demo = new InMemoryUserDataAccessObject();
        Account sark = new Account("sark-asadourian@gmail.com", "password123", "student");
        Account tohya = new Account("henrik-ibsen707@gmail.com", "thewildduck", "student");
        Account isaac = new Account("isaac@gmail.com", "ftlopbd", "teacher");
        Account jed = new Account("jedi@gmail.com", "jediiiiiiiiiii", "teacher");
        demo.save(sark);
        demo.save(tohya);
        demo.save(isaac);
        demo.save(jed);

        LoginView.main(args);
    }
}
